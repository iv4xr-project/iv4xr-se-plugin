using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;

namespace Iv4xr.SePlugin.Control
{
    public abstract class AbstractScreen<TScreen, TData>
            where TScreen : MyGuiScreenBase
            where TData : class
    {
        protected enum ScreenCloseType
        {
            None,
            Now,
            Normal,
        }

        protected TScreen Screen => MyGuiScreenExtensions.EnsureFocusedScreen<TScreen>();

        private readonly ScreenCloseType m_screenClose;

        protected AbstractScreen(ScreenCloseType screenCloseType = ScreenCloseType.None)
        {
            m_screenClose = screenCloseType;
        }

        public TData Data()
        {
            throw new NotImplementedException($"Data not implemented for screen {Screen.DisplayName()}");
        }

        public void Close()
        {
            switch (m_screenClose)
            {
                case ScreenCloseType.Now:
                    Screen.CloseScreenNow();
                    break;
                case ScreenCloseType.Normal:
                    Screen.CloseScreen();
                    break;
                default:
                    throw new InvalidOperationException($"Unable to close window {Screen.DisplayName()}");
            }
        }
    }

    public class MedicalsScreen : AbstractScreen<MyGuiScreenMedicals, object>, IMedicals
    {
        public void Respawn(int roomIndex)
        {
            Screen.Table("m_respawnsTable").SelectedRowIndex = roomIndex;
            Screen.ClickButton("m_respawnButton");
        }

        public void ChooseFaction(int factionIndex)
        {
            Screen.Table("m_factionsTable").SelectedRowIndex = factionIndex;
            Screen.ClickButton("m_selectFactionButton");
        }

        public List<MedicalRoom> MedicalRooms()
        {
            return MedicalRoomRows().Select(row => new MedicalRoom()
                    {
                        Name = row.GetCell(0).Text.ToString(),
                        AvailableIn = row.GetCell(1).Text.ToString()
                    }
            ).ToList();
        }

        public List<Faction> Factions()
        {
            return FactionRows().Select(row => new Faction()
                    {
                        Tag = row.GetCell(0).Text?.ToString() ?? "",
                        Name = row.GetCell(1).Text?.ToString() ?? "",
                    }
            ).ToList();
        }

        private List<MyGuiControlTable.Row> MedicalRoomRows()
        {
            return Screen.Table("m_respawnsTable").RowsAsList();
        }

        private List<MyGuiControlTable.Row> FactionRows()
        {
            return Screen.Table("m_factionsTable").RowsAsList();
        }
    }

    public class Screens : IScreens
    {
        private readonly TerminalScreen m_terminalScreen;
        private readonly MedicalsScreen m_medicalsScreen = new MedicalsScreen();
        private readonly MainMenuScreen m_mainMenuScreen = new MainMenuScreen();
        private readonly MessageBoxScreen m_messageBoxScreen = new MessageBoxScreen();
        private readonly JoinGameScreen m_joinGameScreen = new JoinGameScreen();
        private readonly ServerConnectScreen m_serverConnectScreen = new ServerConnectScreen();

        public Screens()
        {
            m_terminalScreen = new TerminalScreen();
        }

        public IMedicals Medicals => m_medicalsScreen;
        public ITerminal Terminal => m_terminalScreen;
        public IMainMenu MainMenu => m_mainMenuScreen;
        public IMessageBox MessageBox => m_messageBoxScreen;
        public IJoinGame JoinGame => m_joinGameScreen;
        public IServerConnect ServerConnect => m_serverConnectScreen;

        [RunOutsideGameLoop]
        public string FocusedScreen()
        {
            return MyScreenManager.GetScreenWithFocus().DisplayName();
        }

        [RunOutsideGameLoop]
        public void WaitUntilTheGameLoaded()
        {
            const int timeoutMs = 60_000;
            const int singleSleepMs = 100;
            const int sleepAfter = 2000;
            for (var i = 0; i < timeoutMs / singleSleepMs; i++)
            {
                Thread.Sleep(singleSleepMs);
                if (!(MyScreenManager.GetScreenWithFocus() is MyGuiScreenLoading))
                {
                    Thread.Sleep(sleepAfter);
                    return;
                }
            }

            throw new TimeoutException();
        }
    }

    public class ServerConnectScreen : AbstractScreen<MyGuiScreenServerConnect, ServerConnectData>, IServerConnect
    {
        public new ServerConnectData Data()
        {
            return new ServerConnectData()
            {
                    Address = Screen.Controls.TextBox("m_addrTextbox").Text,
                    AddServerToFavorites =  Screen.CheckBox("m_favoriteCheckbox").IsChecked
            };
        }
        
        
        [RunOutsideGameLoop]
        public void ToggleAddServerToFavorites()
        {
            Screen.ClickCheckBox("m_favoriteCheckbox");
        }

        [RunOutsideGameLoop]
        public void Connect()
        {
            Screen.Controls.ButtonByText(MyCommonTexts.MultiplayerJoinConnect).PressButton();
        }

        [RunOutsideGameLoop]
        public void EnterAddress(string address)
        {
            Screen.EnterText("m_addrTextbox", address);
        }
    }

    public class JoinGameScreen : AbstractScreen<MyGuiScreenJoinGame, object>, IJoinGame
    {
        [RunOutsideGameLoop]
        public void DirectConnect()
        {
            Screen.ClickButton("m_directConnectButton");
        }
    }

    public class MessageBoxScreen : AbstractScreen<MyGuiScreenMessageBox, MessageBoxData>, IMessageBox
    {
        [RunOutsideGameLoop]
        public void PressYes()
        {
            Screen.ClickButton("m_yesButton");
        }

        [RunOutsideGameLoop]
        public void PressNo()
        {
            Screen.ClickButton("m_noButton");
        }

        [RunOutsideGameLoop]
        public new MessageBoxData Data()
        {
            var screen = Screen;
            return new MessageBoxData()
            {
                Text = screen.MessageText.ToString(),
                Caption = screen.GetInstanceFieldOrThrow<StringBuilder>("m_messageCaption").ToString(),
                ButtonType = (int)screen.GetInstanceFieldOrThrow<MyMessageBoxButtonsType>("m_buttonType")
            };
        }
    }

    public class MainMenuScreen : AbstractScreen<MyGuiScreenMainMenu, object>, IMainMenu
    {
        private MyGuiControlElementGroup Buttons()
        {
            return Screen
                    .GetInstanceFieldOrThrow<MyGuiControlElementGroup>("m_elementGroup");
        }

        [RunOutsideGameLoop]
        public void Continue()
        {
            Screen.ClickButton("m_continueButton");
        }

        [RunOutsideGameLoop]
        public void NewGame()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonCampaign).PressButton();
        }

        [RunOutsideGameLoop]
        public void LoadGame()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonLoadGame).PressButton();
        }

        [RunOutsideGameLoop]
        public void JoinGame()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonJoinGame).PressButton();
        }

        [RunOutsideGameLoop]
        public void Options()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonOptions).PressButton();
        }

        [RunOutsideGameLoop]
        public void Character()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonInventory).PressButton();
        }

        [RunOutsideGameLoop]
        public void ExitToWindows()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonExitToWindows).PressButton();
        }
    }
}

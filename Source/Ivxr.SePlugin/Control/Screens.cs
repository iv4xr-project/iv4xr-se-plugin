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
    public class MedicalsScreen : IMedicals
    {
        public void Respawn(int roomIndex)
        {
            var screen = EnsureMedicalScreen();
            screen.Table("m_respawnsTable").SelectedRowIndex = roomIndex;
            screen.ClickButton("m_respawnButton");
        }

        public void ChooseFaction(int factionIndex)
        {
            var screen = EnsureMedicalScreen();
            screen.Table("m_factionsTable").SelectedRowIndex = factionIndex;
            screen.ClickButton("m_selectFactionButton");
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

        public T EnsureFocusedScreen<T>() where T : MyGuiScreenBase
        {
            return MyGuiScreenExtensions.EnsureFocusedScreen<T>();
        }


        private MyGuiScreenMedicals EnsureMedicalScreen()
        {
            return EnsureFocusedScreen<MyGuiScreenMedicals>();
        }

        private List<MyGuiControlTable.Row> MedicalRoomRows()
        {
            return EnsureMedicalScreen().Table("m_respawnsTable").RowsAsList();
        }

        private List<MyGuiControlTable.Row> FactionRows()
        {
            return EnsureMedicalScreen().Table("m_factionsTable").RowsAsList();
        }
    }

    public class Screens : IScreens
    {
        private TerminalScreen m_terminalScreen;
        private MedicalsScreen m_medicalsScreen = new MedicalsScreen();
        private MainMenuScreen m_mainMenuScreen = new MainMenuScreen();
        private MessageBoxScreen m_messageBoxScreen = new MessageBoxScreen();
        private JoinGameScreen m_joinGameScreen = new JoinGameScreen();
        private ServerConnectScreen m_serverConnectScreen = new ServerConnectScreen();

        public Screens(GameSession gameSession, LowLevelObserver lowLevelObserver)
        {
            m_terminalScreen = new TerminalScreen(gameSession, lowLevelObserver);
        }

        public IMedicals Medicals => m_medicalsScreen;
        public ITerminal Terminal => m_terminalScreen;
        public IMainMenu MainMenu => m_mainMenuScreen;
        public IMessageBox MessageBox => m_messageBoxScreen;
        public IJoinGame JoinGame => m_joinGameScreen;
        public IServerConnect ServerConnect => m_serverConnectScreen;

        private MyGuiScreenBase ScreenWithFocus()
        {
            return MyScreenManager.GetScreenWithFocus();
        }

        [RunOutsideGameLoop]
        public string FocusedScreen()
        {
            return MyScreenManager.GetScreenWithFocus().DisplayName();
        }

        [RunOutsideGameLoop]
        public void WaitUntilTheGameLoaded()
        {
            var timeoutMs = 60_000;
            var singleSleepMs = 100;
            var sleepAfter = 2000;
            for (var i = 0; i < timeoutMs / singleSleepMs; i++)
            {
                Thread.Sleep(singleSleepMs);
                if (!(ScreenWithFocus() is MyGuiScreenLoading))
                {
                    Thread.Sleep(sleepAfter);
                    return;
                }
            }

            throw new TimeoutException();
        }
    }

    public class ServerConnectScreen : IServerConnect
    {
        private MyGuiScreenServerConnect Screen => MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenServerConnect>();

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

    public class JoinGameScreen : IJoinGame
    {
        private MyGuiScreenJoinGame Screen => MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenJoinGame>();
        
        [RunOutsideGameLoop]
        public void DirectConnect()
        {
            Screen.ClickButton("m_directConnectButton");
        }
    }

    public class MessageBoxScreen : IMessageBox
    {
        private MyGuiScreenMessageBox Screen()
        {
            return MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenMessageBox>();
        }
        
        [RunOutsideGameLoop]
        public void PressYes()
        {
            Screen().ClickButton("m_yesButton");
        }

        [RunOutsideGameLoop]
        public void PressNo()
        {
            Screen().ClickButton("m_noButton");
        }

        [RunOutsideGameLoop]
        public MessageBoxData Data()
        {
            var screen = Screen();
            return new MessageBoxData()
            {
                Text = screen.MessageText.ToString(),
                Caption = screen.GetInstanceFieldOrThrow<StringBuilder>("m_messageCaption").ToString(),
                ButtonType = (int) screen.GetInstanceFieldOrThrow<MyMessageBoxButtonsType>("m_buttonType")
            };
        }
    }

    public class MainMenuScreen : IMainMenu
    {
        private MyGuiScreenMainMenu Screen()
        {
            return MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenMainMenu>();
        }

        private MyGuiControlElementGroup Buttons()
        {
            return Screen()
                    .GetInstanceFieldOrThrow<MyGuiControlElementGroup>("m_elementGroup");
        }
        
        [RunOutsideGameLoop]
        public void Continue()
        {
            var screen = Screen();
            screen.ClickButton("m_continueButton");
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

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Gui;
using Sandbox.Game.GUI;
using Sandbox.Game.Screens;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;
using VRage;
using VRage.Game;
using VRage.Game.Entity;
using VRage.Input;
using VRage.ModAPI;
using VRage.Utils;
using VRage.Voxels;
using VRageMath;
using static Iv4xr.SePlugin.Communication.CallTarget;

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

        protected void CheckScreen()
        {
            MyGuiScreenExtensions.EnsureFocusedScreen<TScreen>();
        }

        private readonly ScreenCloseType m_screenClose;

        protected AbstractScreen(ScreenCloseType screenCloseType = ScreenCloseType.None)
        {
            m_screenClose = screenCloseType;
        }

        public virtual TData Data()
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
        private readonly NewGameScreen m_newGameScreen = new NewGameScreen();
        private readonly LoadGameScreen m_loadGameScreen = new LoadGameScreen();
        private readonly GamePlayScreen m_gamePlayScreen = new GamePlayScreen();

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
        public ILoadGame LoadGame => m_loadGameScreen;
        public INewGame NewGame => m_newGameScreen;
        public IGamePlay GamePlay => m_gamePlayScreen;

        [CallOn(CurrentThread)]
        public string FocusedScreen()
        {
            return MyScreenManager.GetScreenWithFocus().DisplayName();
        }

        [CallOn(CurrentThread)]
        public void WaitUntilTheGameLoaded()
        {
            const int timeoutMs = 60_000;
            const int singleSleepMs = 100;
            const int sleepAfter = 2000;
            for (var i = 0; i < timeoutMs / singleSleepMs; i++)
            {
                Thread.Sleep(singleSleepMs);
                if (!(MyScreenManager.GetScreenWithFocus() is MyGuiScreenLoading) && MyVRage.Platform.SessionReady)
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
        public override ServerConnectData Data()
        {
            return new ServerConnectData()
            {
                Address = Screen.Controls.TextBox("m_addrTextbox").Text,
                AddServerToFavorites = Screen.CheckBox("m_favoriteCheckbox").IsChecked
            };
        }

        public void ToggleAddServerToFavorites()
        {
            Screen.ClickCheckBox("m_favoriteCheckbox");
        }

        public void Connect()
        {
            Screen.Controls.ButtonByText(MyCommonTexts.MultiplayerJoinConnect).PressButton();
        }

        public void EnterAddress(string address)
        {
            Screen.EnterText("m_addrTextbox", address);
        }
    }

    public class JoinGameScreen : AbstractScreen<MyGuiScreenJoinGame, JoinGameData>, IJoinGame
    {
        public void DirectConnect()
        {
            Screen.ClickButton("m_directConnectButton");
        }

        public void JoinWorld()
        {
            Screen.ClickButton("m_joinButton");
        }

        public void Refresh()
        {
            Screen.ClickButton("m_refreshButton");
        }

        public void ServerDetails()
        {
            Screen.ClickButton("m_detailsButton");
        }

        private MyGuiControlTabControl Tabs =>
                Screen.GetInstanceFieldOrThrow<MyGuiControlTabControl>("m_joinGameTabs");


        public void SelectTab(int index)
        {
            Tabs.SelectedPage = index;
        }

        public void SelectGame(int index)
        {
            GamesTable.SelectedRowIndex = index;
            Screen.CallMethod<object>("OnTableItemSelected", new object[]
            {
                GamesTable, new MyGuiControlTable.EventArgs()
                {
                    RowIndex = index,
                    MouseButton = MyMouseButtonsEnum.Left,
                }
            });
        }

        private MyGuiControlTable GamesTable =>
                Screen.GetInstanceFieldOrThrow<MyGuiControlTable>("m_gamesTable");

        private List<ListedGameInformation> ListedGames()
        {
            return GamesTable.Rows.Select(row =>
            {
                var cells = row.GetInstanceFieldOrThrow<List<MyGuiControlTable.Cell>>("Cells");
                return new ListedGameInformation()
                {
                    World = cells[3].Text.ToString(),
                    Server = cells[6].Text.ToString(),
                    //4 mode
                };
            }).ToList();
        }

        public override JoinGameData Data()
        {
            return new JoinGameData()
            {
                SelectedTab = Tabs.Pages[Tabs.SelectedPage].Name.Replace("Page", ""),
                Games = ListedGames(),
            };
        }
    }

    public class MessageBoxScreen : AbstractScreen<MyGuiScreenMessageBox, MessageBoxData>, IMessageBox
    {
        public void PressYes()
        {
            Screen.ClickButton("m_yesButton");
        }

        public void PressNo()
        {
            Screen.ClickButton("m_noButton");
        }

        public override MessageBoxData Data()
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

        public void Continue()
        {
            Screen.ClickButton("m_continueButton");
        }

        public void NewGame()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonCampaign).PressButton();
        }

        public void LoadGame()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonLoadGame).PressButton();
        }

        public void JoinGame()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonJoinGame).PressButton();
        }

        public void Options()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonOptions).PressButton();
        }

        public void Character()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonInventory).PressButton();
        }

        public void ExitToWindows()
        {
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonExitToWindows).PressButton();
        }

        public void ExitToMainMenu()
        {
            CheckScreen();
            Buttons().ButtonByText(MyCommonTexts.ScreenMenuButtonExitToMainMenu).PressButton();
        }
    }

    public class NewGameScreen : AbstractScreen<MyGuiScreenNewGame, object>, INewGame
    {
    }

    public class LoadGameScreen : AbstractScreen<MyGuiScreenLoadSandbox, LoadGameData>, ILoadGame
    {
        private MyGuiControlSaveBrowser Browser =>
                Screen.GetInstanceFieldOrThrow<MyGuiControlSaveBrowser>("m_saveBrowser");

        public override LoadGameData Data()
        {
            var rows = Browser.RowsAsList().Select(
                row =>
                {
                    var cellText = row.GetCell(0).Text.ToString();
                    if (row.UserData is FileInfo info)
                    {
                        var file = info.ToFile();
                        file.Name = cellText;
                        return file;
                    }
                    else if (row.UserData is DirectoryInfo directoryInfo)
                    {
                        return directoryInfo.ToFile();
                    }
                    else if (row.UserData == null && cellText == "..")
                    {
                        var currDir = Browser.GetInstanceFieldOrThrow<DirectoryInfo>("m_currentDir");
                        var file = currDir.Parent.ToFile();
                        file.Name = "..";
                        return file;
                    }

                    throw new InvalidDataException("Unable to convert data from ControlDirectoryBrowser");
                });
            return new LoadGameData()
            {
                CurrentDirectory = Browser.GetInstanceFieldOrThrow<DirectoryInfo>("m_currentDir").ToFile(),
                RootDirectory = Browser.GetInstanceFieldOrThrow<DirectoryInfo>("m_topMostDir").ToFile(),
                Files = rows.ToList(),
            };
        }

        public void Filter(string text)
        {
            Screen.EnterSearchText("m_searchBox", text);
        }

        public void DoubleClickWorld(int index)
        {
            Browser.SelectedRowIndex = index;
            MyGuiControlTable browserAsTable = Browser;
            browserAsTable.CallMethod<object>("OnItemDoubleClicked",
                new object[]
                {
                    Browser,
                    new MyGuiControlTable.EventArgs() { RowIndex = index }
                }
            );
        }

        public void Load()
        {
            Screen.ClickButton("m_loadButton");
        }

        public void Edit()
        {
            Screen.ClickButton("m_editButton");
        }

        public void Delete()
        {
            Screen.ClickButton("m_deleteButton");
        }

        public void Save()
        {
            Screen.ClickButton("m_saveButton");
        }

        public void Publish()
        {
            Screen.ClickButton("m_publishButton");
        }
    }

    public class GamePlayScreen : AbstractScreen<MyGuiScreenGamePlay, GamePlayData>, IGamePlay
    {
        class PositionedOreMarker
        {
            public Vector3D Position;
            public MyEntityOreDeposit OreDeposit;
            public string Name;
            public double Distance;
        }


        //based on MyGuiScreenHudSpace.DrawOreMarkers
        private IEnumerable<PositionedOreMarker> CreateMarkers()
        {
            MyHudOreMarkers oreMarkers = MyHud.OreMarkers;
            Vector3D controlledEntityPosition = Vector3D.Zero;
            if (MySession.Static != null && MySession.Static.ControlledEntity != null)
            {
                controlledEntityPosition = ((MyEntity)MySession.Static.ControlledEntity).WorldMatrix.Translation;
            }

            PositionedOreMarker[] nearestOreDeposits =
                    new PositionedOreMarker[MyDefinitionManager.Static.VoxelMaterialCount];
            float[] nearestDistancesSquared = new float[nearestOreDeposits.Length];

            for (int i = 0; i < nearestOreDeposits.Length; i++)
            {
                nearestOreDeposits[i] = null;
                nearestDistancesSquared[i] = float.MaxValue;
            }


            foreach (MyEntityOreDeposit oreMarker in oreMarkers)
            {
                for (int i = 0; i < oreMarker.Materials.Count; i++)
                {
                    MyEntityOreDeposit.Data depositData = oreMarker.Materials[i];

                    var oreMaterial = depositData.Material;
                    Vector3D oreWorldPosition;
                    var voxelMap = oreMarker.VoxelMap;
                    MyVoxelCoordSystems.LocalPositionToWorldPosition(
                        (voxelMap.PositionComp.GetPosition() - (Vector3D)voxelMap.StorageMin),
                        ref depositData.AverageLocalPosition, out oreWorldPosition);

                    //ProfilerShort.BeginNextBlock("Distance");
                    Vector3D diff = (controlledEntityPosition - oreWorldPosition);
                    float distanceSquared = (float)diff.LengthSquared();

                    float nearestDistanceSquared = nearestDistancesSquared[oreMaterial.Index];
                    if (distanceSquared < nearestDistanceSquared)
                    {
                        MyVoxelMaterialDefinition voxelMaterial =
                                MyDefinitionManager.Static.GetVoxelMaterialDefinition(oreMaterial.Index);
                        nearestOreDeposits[oreMaterial.Index] = new PositionedOreMarker()
                        {
                            Position = oreWorldPosition,
                            OreDeposit = oreMarker,
                            Name = oreMarkers.GetOreName(voxelMaterial),
                            Distance = diff.Length(),
                        };
                        nearestDistancesSquared[oreMaterial.Index] = distanceSquared;
                    }
                }
            }

            return nearestOreDeposits.Where(nod =>
                    nod?.OreDeposit?.VoxelMap != null && !nod.OreDeposit.VoxelMap.Closed);
        }

        public override GamePlayData Data()
        {
            return new GamePlayData()
            {
                OreMarkers = CreateMarkers().Select(positionedOreMarker =>
                        new OreMarker()
                        {
                            Text = positionedOreMarker.Name,
                            Position = positionedOreMarker.Position.ToPlain(),
                            Distance = positionedOreMarker.Distance,
                            Materials = positionedOreMarker.OreDeposit.Materials
                                    .Select(data => data.Material.ToDefinitionId()).ToList(),
                        }
                ).ToList(),
                Hud = new Hud()
                {
                    Stats = MyHud.Stats.GetInstanceField<Dictionary<MyStringHash, IMyHudStat>>("m_stats").Values
                            .ToDictionary(
                                x => x.GetType().Name.Replace("MyStat", ""),
                                x => x.CurrentValue
                            )
                }
            };
        }

        public void ShowMainMenu()
        {
            CheckScreen();
            MyGuiSandbox.AddScreen(MyGuiSandbox.CreateScreen(MyPerGameSettings.GUI.MainMenu,
                MySandboxGame.IsPaused == false));
        }
    }
}

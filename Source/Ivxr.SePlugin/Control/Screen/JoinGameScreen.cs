using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;
using VRage.Input;

namespace Iv4xr.SePlugin.Control.Screen
{
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
            Tabs.SelectedPage = index.CheckIndex();
        }

        public void SelectGame(int index)
        {
            GamesTable.SelectedRowIndex = index.CheckIndex();
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
                var cells = ReflectionExtensions.GetInstanceFieldOrThrow<List<MyGuiControlTable.Cell>>(row, "Cells");
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
}

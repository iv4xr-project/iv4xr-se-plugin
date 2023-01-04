using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class TerminalScreen : AbstractScreen<MyGuiScreenTerminal, TerminalScreenData>, ITerminal
    {
        public TerminalScreen() : base(ScreenCloseType.Normal)
        {
        }

        public override TerminalScreenData Data()
        {
            return new TerminalScreenData()
            {
                SelectedTab = Screen.GetTabs().Pages[Screen.GetTabs().SelectedPage].Name.Replace("Page", ""),
            };
        }

        public void SelectTab(int index)
        {
            Screen.GetTabs().SelectedPage = index.CheckIndex();;
        }

        public IInventoryTab Inventory { get; } = new InventoryTab();
        public IControlPanelTab ControlPanel { get; } = new ControlPanelTab();
        public IProductionTab Production { get; } = new ProductionTab();
        public IInfoTab Info { get; } = new InfoTab();
        public IFactionsTab Factions { get; } = new FactionsTab();
        public ICommsTab Comms { get; } = new CommsTab();
        public IGpsTab Gps { get; } = new GpsTab();
        public IRemoteAccess RemoteAccess { get; } = new RemoteAccess();
    }
}

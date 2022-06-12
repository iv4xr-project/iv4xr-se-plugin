using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
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
            Screen.GetTabs().SelectedPage = index;
        }

        public IInventoryTab Inventory { get; } = new InventoryTab();
        public IControlPanelTab ControlPanel { get; } = new ControlPanelTab();
        public IProductionTab Production { get; } = new ProductionTab();
        public IInfoTab Info { get; } = new InfoTab();
        public IFactionsTab Factions { get; } = new FactionsTab();
        public ICommsTab Comms { get; } = new CommsTab();
        public IGpsTab Gps { get; } = new GpsTab();

        private static List<MyInventory> Inventories(MyGuiControlList inventoriesControl)
        {
            var dstControlEnumerator = inventoriesControl.Controls.GetEnumerator();
            List<MyInventory> availableInventories = new List<MyInventory>();

            MyGuiControlInventoryOwner dstControl = null;
            while (dstControlEnumerator.MoveNext())
            {
                if (dstControlEnumerator.Current.Visible)
                {
                    dstControl = dstControlEnumerator.Current as MyGuiControlInventoryOwner;

                    if (dstControl == null || !dstControl.Enabled)
                    {
                        continue;
                    }

                    var dstOwner = dstControl.InventoryOwner;

                    for (int i = 0; i < dstOwner.InventoryCount; ++i)
                    {
                        var tmp = dstOwner.GetInventory(i);

                        if (tmp != null)
                        {
                            availableInventories.Add(tmp);
                        }
                    }
                }
            }

            return availableInventories;
        }

        internal static List<MyInventory> LeftInventories()
        {
            var inventoriesControl = TerminalInventoryController()
                    .GetInstanceFieldOrThrow<MyGuiControlList>("m_leftOwnersControl");
            return Inventories(inventoriesControl);
        }

        internal static List<MyInventory> RightInventories()
        {
            var inventoriesControl = TerminalInventoryController()
                    .GetInstanceFieldOrThrow<MyGuiControlList>("m_rightOwnersControl");
            return Inventories(inventoriesControl);
        }

        internal static object TerminalInventoryController()
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            return screen.GetInstanceFieldOrThrow<object>("m_controllerInventory");
        }
    }
}

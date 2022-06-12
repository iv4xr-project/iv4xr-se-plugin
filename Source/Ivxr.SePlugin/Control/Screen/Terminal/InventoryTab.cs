using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class InventoryTab : AbstractTerminalTab<TerminalInventoryData>, IInventoryTab
    {
        public InventoryTab() : base(MyTerminalPageEnum.Inventory, "m_controllerInventory")
        {
        }

        public override TerminalInventoryData Data()
        {
            var controller = UntypedController;
            var inventories = controller.CallMethod<MyInventory[]>("GetSourceInventories");
            var rightInventories = RightInventories();
            return new TerminalInventoryData()
            {
                LeftInventories = inventories.Select(i => MyExtensions.ToInventory(i)).ToList(),
                RightInventories = rightInventories.Select(i => i.ToInventory()).ToList(),
            };
        }

        private class LeftInventory : IInventorySide
        {
            MyGuiControlGrid grid => TerminalInventoryController().Grid("m_leftFocusedInventory");

            private void ClickRadio(string fieldName)
            {
                TerminalInventoryController().ClickRadio(fieldName);
            }

            public void Filter(string text)
            {
                TerminalInventoryController().EnterSearchText("m_searchBoxLeft", text);
            }

            public void SwapToGrid()
            {
                ClickRadio("m_leftGridButton");
            }

            public void SwapToCharacterOrItem()
            {
                ClickRadio("m_leftSuitButton");
            }

            public void SelectItem(int index)
            {
                grid.SelectedIndex = index;
            }

            public void ClickSelectedItem()
            {
                grid.ClickItem();
            }

            public void DoubleClickSelectedItem()
            {
                grid.DoubleClickItem();
            }

            public void FilterAll()
            {
                ClickRadio("m_leftFilterAllButton");
            }

            public void FilterEnergy()
            {
                ClickRadio("m_leftFilterEnergyButton");
            }

            public void FilterShip()
            {
                ClickRadio("m_leftFilterShipButton");
            }

            public void FilterSystem()
            {
                ClickRadio("m_leftFilterSystemButton");
            }

            public void FilterStorage()
            {
                ClickRadio("m_leftFilterStorageButton");
            }

            public void ToggleHideEmpty()
            {
                TerminalInventoryController().ClickCheckBox("m_hideEmptyLeft");
            }
        }

        private class RightInventory : IInventorySide
        {
            MyGuiControlGrid grid => TerminalInventoryController().Grid("m_rightFocusedInventory");

            private void ClickRadio(string fieldName)
            {
                TerminalInventoryController().ClickRadio(fieldName);
            }

            public void Filter(string text)
            {
                TerminalInventoryController().EnterSearchText("m_searchBoxRight", text);
            }

            public void SwapToGrid()
            {
                ClickRadio("m_rightGridButton");
            }

            public void SwapToCharacterOrItem()
            {
                ClickRadio("m_rightSuitButton");
            }

            public void SelectItem(int index)
            {
                grid.SelectedIndex = index;
            }

            public void ClickSelectedItem()
            {
                grid.CallMethod<object>("TryTriggerSingleClickEvent");
            }

            public void DoubleClickSelectedItem()
            {
                grid.CallMethod<object>("DoubleClickItem");
            }

            public void FilterAll()
            {
                ClickRadio("m_rightFilterAllButton");
            }

            public void FilterEnergy()
            {
                ClickRadio("m_rightFilterEnergyButton");
            }

            public void FilterShip()
            {
                ClickRadio("m_rightFilterShipButton");
            }

            public void FilterSystem()
            {
                ClickRadio("m_rightFilterSystemButton");
            }

            public void FilterStorage()
            {
                ClickRadio("m_rightFilterStorageButton");
            }

            public void ToggleHideEmpty()
            {
                TerminalInventoryController().ClickCheckBox("m_hideEmptyRight");
            }
        }

        public void TransferInventoryItem(int sourceInventoryId, int destinationInventoryId, int itemId)
        {
            var sourceInventory = LeftInventories()[sourceInventoryId];
            var rightInventory = RightInventories();
            var destinationInventory = rightInventory[destinationInventoryId];
            MyInventory.TransferByUser(sourceInventory, destinationInventory, (uint)itemId,
                destinationInventory.ItemCount);
            UntypedController.CallMethod<object>("RefreshSelectedInventoryItem", new object[] { });
        }

        public void DropSelected()
        {
            UntypedController.ClickButton("m_throwOutButton");
        }

        public void Withdraw()
        {
            UntypedController.ClickButton("m_withdrawButton");
        }

        public void Deposit()
        {
            UntypedController.ClickButton("m_depositAllButton");
        }

        public void FromBuildPlannerToProductionQueue()
        {
            UntypedController.ClickButton("m_addToProductionButton");
        }

        public void SelectedToProductionQueue()
        {
            UntypedController.ClickButton("m_selectedToProductionButton");
        }

        public IInventorySide Left { get; } = new LeftInventory();

        public IInventorySide Right { get; } = new RightInventory();
        
        
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

        private static List<MyInventory> LeftInventories()
        {
            var inventoriesControl = TerminalInventoryController()
                    .GetInstanceFieldOrThrow<MyGuiControlList>("m_leftOwnersControl");
            return Inventories(inventoriesControl);
        }

        private static List<MyInventory> RightInventories()
        {
            var inventoriesControl = TerminalInventoryController()
                    .GetInstanceFieldOrThrow<MyGuiControlList>("m_rightOwnersControl");
            return Inventories(inventoriesControl);
        }

        private static object TerminalInventoryController()
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            return screen.GetInstanceFieldOrThrow<object>("m_controllerInventory");
        }
        
    }
}

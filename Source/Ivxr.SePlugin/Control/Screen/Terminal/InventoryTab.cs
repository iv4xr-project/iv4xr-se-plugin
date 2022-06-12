using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class InventoryTab : AbstractScreen<MyGuiScreenTerminal, TerminalInventoryData>, IInventoryTab
    {
        public override TerminalInventoryData Data()
        {
            var controller = TerminalInventoryController();
            var inventories = controller.CallMethod<MyInventory[]>("GetSourceInventories");
            var rightInventories = TerminalScreen.RightInventories();
            return new TerminalInventoryData()
            {
                LeftInventories = inventories.Select(i => MyExtensions.ToInventory(i)).ToList(),
                RightInventories = rightInventories.Select(i => i.ToInventory()).ToList(),
            };
        }

        private class LeftInventory : IInventorySide
        {
            MyGuiControlGrid grid => TerminalScreen.TerminalInventoryController().Grid("m_leftFocusedInventory");

            private void ClickRadio(string fieldName)
            {
                TerminalScreen.TerminalInventoryController().ClickRadio(fieldName);
            }

            public void Filter(string text)
            {
                TerminalScreen.TerminalInventoryController().EnterSearchText("m_searchBoxLeft", text);
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
                TerminalScreen.TerminalInventoryController().ClickCheckBox("m_hideEmptyLeft");
            }
        }

        private class RightInventory : IInventorySide
        {
            MyGuiControlGrid grid => TerminalScreen.TerminalInventoryController().Grid("m_rightFocusedInventory");

            private void ClickRadio(string fieldName)
            {
                TerminalScreen.TerminalInventoryController().ClickRadio(fieldName);
            }

            public void Filter(string text)
            {
                TerminalScreen.TerminalInventoryController().EnterSearchText("m_searchBoxRight", text);
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
                TerminalScreen.TerminalInventoryController().ClickCheckBox("m_hideEmptyRight");
            }
        }

        private object TerminalInventoryController()
        {
            return TerminalScreen.TerminalInventoryController();
        }

        public void TransferInventoryItem(int sourceInventoryId, int destinationInventoryId, int itemId)
        {
            var sourceInventory = TerminalScreen.LeftInventories()[sourceInventoryId];
            var rightInventory = TerminalScreen.RightInventories();
            var destinationInventory = rightInventory[destinationInventoryId];
            MyInventory.TransferByUser(sourceInventory, destinationInventory, (uint)itemId,
                destinationInventory.ItemCount);
            TerminalInventoryController().CallMethod<object>("RefreshSelectedInventoryItem", new object[] { });
        }

        public void DropSelected()
        {
            TerminalScreen.TerminalInventoryController().ClickButton("m_throwOutButton");
        }

        public void Withdraw()
        {
            TerminalScreen.TerminalInventoryController().ClickButton("m_withdrawButton");
        }

        public void Deposit()
        {
            TerminalScreen.TerminalInventoryController().ClickButton("m_depositAllButton");
        }

        public void FromBuildPlannerToProductionQueue()
        {
            TerminalScreen.TerminalInventoryController().ClickButton("m_addToProductionButton");
        }

        public void SelectedToProductionQueue()
        {
            TerminalScreen.TerminalInventoryController().ClickButton("m_selectedToProductionButton");
        }

        public IInventorySide Left { get; } = new LeftInventory();

        public IInventorySide Right { get; } = new RightInventory();
    }
}
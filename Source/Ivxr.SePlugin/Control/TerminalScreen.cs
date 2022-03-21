using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage;
using VRage.Game.Entity;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control
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

        public IProductionTab Production { get; } = new ProductionTab();

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

    public class ProductionTab : AbstractScreen<MyGuiScreenTerminal, TerminalProductionData>, IProductionTab
    {
        public override TerminalProductionData Data()
        {
            var productionTab = Screen.ProductionTab();

            var productionQueueGrid = productionTab.TabControlByName<MyGuiControlScrollablePanel>("QueueScrollableArea")
                    .ScrollableChild<MyGuiControlGrid>();
            var productionQueueItems = productionQueueGrid.GridItemUserDataOfType<MyProductionBlock.QueueItem>()
                    .Select(bp => bp.ToProductionQueueItem()).ToList();


            var inventoryGrid = productionTab.TabControlByName<MyGuiControlScrollablePanel>("InventoryScrollableArea")
                    .ScrollableChild<MyGuiControlGrid>();
            var inventoryItems = inventoryGrid.GridItemUserDataOfType<MyPhysicalInventoryItem>()
                    .Select(piItem => piItem.ToAmountedDefinition())
                    .ToList();

            var blueprintsGrid =
                    productionTab.TabControlByName<MyGuiControlScrollablePanel>("BlueprintsScrollableArea")
                            .ScrollableChild<MyGuiControlGrid>();
            var blueprintItems = blueprintsGrid.GridItemUserDataOfType<MyBlueprintDefinitionBase>()
                    .Select(bp => bp.ToBlueprintDefinition()).ToList();

            var assemblers = Screen.ProductionTab().TabControlByName<MyGuiControlCombobox>("AssemblersCombobox")
                    .ItemsAsList().Select(
                        i => i.Value.ToString()).ToList();

            return new TerminalProductionData()
            {
                Assemblers = assemblers,
                ProductionQueue = productionQueueItems,
                Inventory = inventoryItems,
                Blueprints = blueprintItems,
                ProductionRepeatMode = productionTab.TabControlByName<MyGuiControlCheckbox>("RepeatCheckbox")
                        .IsChecked,
                ProductionCooperativeMode =
                        productionTab.TabControlByName<MyGuiControlCheckbox>("SlaveCheckbox").IsChecked
            };
        }

        public void ToggleProductionRepeatMode()
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var cb = screen.ProductionTab().TabControlByName<MyGuiControlCheckbox>("RepeatCheckbox");
            cb.IsChecked = !cb.IsChecked;
        }

        public void ToggleProductionCooperativeMode()
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var productionTab = screen.GetTabs().Pages[(int)MyTerminalPageEnum.Production];
            var cb = (MyGuiControlCheckbox)productionTab.Controls.GetControlByName("SlaveCheckbox");
            cb.IsChecked = !cb.IsChecked;
        }
        
        [RunOnMainThread]
        public void AddToProductionQueue(int index)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var blueprintsGrid = screen.ProductionTab().TabControlByName<MyGuiControlScrollablePanel>(
                "BlueprintsScrollableArea").ScrollableChild<MyGuiControlGrid>();
            blueprintsGrid.SelectedIndex = index;
            var item = blueprintsGrid.SelectedItem;
            var blueprint = (MyBlueprintDefinitionBase)item.UserData;
            var controller = screen.GetInstanceFieldOrThrow<object>("m_controllerProduction");
            MyFixedPoint one = 1;
            controller.CallMethod<object>("EnqueueBlueprint", new object[] { blueprint, one });
        }

        public void RemoveFromProductionQueue(int index)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var blueprintsGrid = screen.ProductionTab().TabControlByName<MyGuiControlScrollablePanel>(
                "QueueScrollableArea").ScrollableChild<MyGuiControlGrid>();
            blueprintsGrid.SelectedIndex = index;
            MyFixedPoint minusOne = -1;
            var controller = screen.GetInstanceFieldOrThrow<object>("m_controllerProduction");
            var assembler = controller.GetInstanceFieldOrThrow<MyAssembler>("m_selectedAssembler");
            assembler.RemoveQueueItemRequest(index, minusOne);
        }

        public void SelectBlueprint(int index)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var controller = screen.GetInstanceFieldOrThrow<object>("m_controllerProduction");
            var bpg = controller.GetInstanceFieldOrThrow<MyGuiControlRadioButtonGroup>("m_blueprintButtonGroup");
            bpg.SelectByIndex(index);
        }

        public void SelectAssembler(int index)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            screen.ProductionTab().TabControlByName<MyGuiControlCombobox>("AssemblersCombobox")
                    .SelectItemByIndex(index);
        }

        public void EnterBlueprintSearchBox(string text)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var searchBox = screen.ProductionTab().TabControlByName<MyGuiControlSearchBox>(
                "BlueprintsSearchBox");
            searchBox.SearchText = text;
        }
    }

    public class InventoryTab : AbstractScreen<MyGuiScreenTerminal, TerminalInventoryData>, IInventoryTab
    {
        public override TerminalInventoryData Data()
        {
            var controller = TerminalInventoryController();
            var inventories = controller.CallMethod<MyInventory[]>("GetSourceInventories");
            var rightInventories = TerminalScreen.RightInventories();
            return new TerminalInventoryData()
            {
                LeftInventories = inventories.Select(i => i.ToInventory()).ToList(),
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

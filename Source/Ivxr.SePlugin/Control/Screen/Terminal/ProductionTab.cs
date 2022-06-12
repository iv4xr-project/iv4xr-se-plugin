using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage;
using VRage.Game.Entity;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class ProductionTab : AbstractTerminalTab<TerminalProductionData>, IProductionTab
    {
        public ProductionTab() : base(MyTerminalPageEnum.Production)
        {
        }

        public override TerminalProductionData Data()
        {
            var productionQueueGrid = Tab.TabControlByName<MyGuiControlScrollablePanel>("QueueScrollableArea")
                    .ScrollableChild<MyGuiControlGrid>();
            var productionQueueItems = productionQueueGrid.GridItemUserDataOfType<MyProductionBlock.QueueItem>()
                    .Select(bp => MyExtensions.ToProductionQueueItem(bp)).ToList();


            var inventoryGrid = Tab.TabControlByName<MyGuiControlScrollablePanel>("InventoryScrollableArea")
                    .ScrollableChild<MyGuiControlGrid>();
            var inventoryItems = inventoryGrid.GridItemUserDataOfType<MyPhysicalInventoryItem>()
                    .Select(piItem => piItem.ToAmountedDefinition())
                    .ToList();

            var blueprintsGrid =
                    Tab.TabControlByName<MyGuiControlScrollablePanel>("BlueprintsScrollableArea")
                            .ScrollableChild<MyGuiControlGrid>();
            var blueprintItems = blueprintsGrid.GridItemUserDataOfType<MyBlueprintDefinitionBase>()
                    .Select(bp => bp.ToBlueprintDefinition()).ToList();

            var assemblers = Tab.TabControlByName<MyGuiControlCombobox>("AssemblersCombobox")
                    .ItemsAsList().Select(
                        i => i.Value.ToString()).ToList();

            return new TerminalProductionData()
            {
                Assemblers = assemblers,
                ProductionQueue = productionQueueItems,
                Inventory = inventoryItems,
                Blueprints = blueprintItems,
                ProductionRepeatMode = Tab.TabControlByName<MyGuiControlCheckbox>("RepeatCheckbox")
                        .IsChecked,
                ProductionCooperativeMode =
                        Tab.TabControlByName<MyGuiControlCheckbox>("SlaveCheckbox").IsChecked
            };
        }

        public void ToggleProductionRepeatMode()
        {
            var cb = Tab.TabControlByName<MyGuiControlCheckbox>("RepeatCheckbox");
            cb.IsChecked = !cb.IsChecked;
        }

        public void ToggleProductionCooperativeMode()
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var productionTab = screen.GetTabs().Pages[(int)MyTerminalPageEnum.Production];
            var cb = (MyGuiControlCheckbox)productionTab.Controls.GetControlByName("SlaveCheckbox");
            cb.IsChecked = !cb.IsChecked;
        }
        
        public void AddToProductionQueue(int index)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var blueprintsGrid = Tab.TabControlByName<MyGuiControlScrollablePanel>(
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
            var blueprintsGrid = Tab.TabControlByName<MyGuiControlScrollablePanel>(
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
            Tab.TabControlByName<MyGuiControlCombobox>("AssemblersCombobox")
                    .SelectItemByIndex(index);
        }

        public void EnterBlueprintSearchBox(string text)
        {
            var searchBox = Tab.TabControlByName<MyGuiControlSearchBox>(
                "BlueprintsSearchBox");
            searchBox.SearchText = text;
        }
    }
}

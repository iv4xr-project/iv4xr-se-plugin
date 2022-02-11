using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;
using VRage;
using VRage.Game.Entity;
using VRage.Game.ModAPI;
using VRage.Game.ModAPI.Ingame;

namespace Iv4xr.SePlugin.Control
{
    public class TerminalScreen : ITerminal
    {
        public TerminalScreenData Data()
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var productionTab = screen.ProductionTab();

            var productionQueueGrid = productionTab.TabControlByName<MyGuiControlScrollablePanel>("QueueScrollableArea")
                    .ScrollableChild<MyGuiControlGrid>();
            var productionQueueItems = productionQueueGrid.GridItemUserDataOfType<MyProductionBlock.QueueItem>()
                    .Select(item => item.Blueprint).Select(bp => bp.ToProductionQueueItem()).ToList();


            var inventoryGrid = productionTab.TabControlByName<MyGuiControlScrollablePanel>("InventoryScrollableArea")
                    .ScrollableChild<MyGuiControlGrid>();
            var inventoryItems = inventoryGrid.GridItemUserDataOfType<MyPhysicalInventoryItem>()
                    .Select(piItem => piItem.ToAmountedDefinition())
                    .ToList();

            var blueprintsGrid =
                    productionTab.TabControlByName<MyGuiControlScrollablePanel>("BlueprintsScrollableArea")
                            .ScrollableChild<MyGuiControlGrid>();
            var blueprintItems = blueprintsGrid.GridItemUserDataOfType<MyBlueprintDefinition>()
                    .Select(bp => bp.ToProductionQueueItem()).ToList();

            var assemblers = screen.ProductionTab().TabControlByName<MyGuiControlCombobox>("AssemblersCombobox").ItemsAsList().Select(
                i => i.Value.ToString()).ToList();

            return new TerminalScreenData()
            {
                Assemblers = assemblers,
                ProductionQueue = productionQueueItems,
                Inventory = inventoryItems,
                Blueprints = blueprintItems,
                ProductionRepeatMode = productionTab.TabControlByName<MyGuiControlCheckbox>("RepeatCheckbox")
                        .IsChecked,
                ProductionCooperativeMode =
                        productionTab.TabControlByName<MyGuiControlCheckbox>("SlaveCheckbox").IsChecked,
                SelectedTab = screen.GetTabs().Pages[screen.GetTabs().SelectedPage].Name.Replace("Page", ""),
            };
        }

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
            controller.CallMethod("EnqueueBlueprint", new object[] { blueprint, one });
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
            screen.ProductionTab().TabControlByName<MyGuiControlCombobox>("AssemblersCombobox").SelectItemByIndex(index);
        }

        public void EnterBlueprintSearchBox(string text)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            var searchBox = screen.ProductionTab().TabControlByName<MyGuiControlSearchBox>(
                "BlueprintsSearchBox");
            searchBox.SearchText = text;
        }

        public void SelectTab(int index)
        {
            var screen = MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenTerminal>();
            screen.GetTabs().SelectedPage = index;
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
    }

    public class MedicalsScreen : IMedicals
    {
        public void Respawn(int roomIndex)
        {
            var screen = EnsureMedicalScreen();
            screen.Table("m_respawnsTable").SelectedRowIndex = roomIndex;
            screen.Button("m_respawnButton").PressButton();
        }

        public void ChooseFaction(int factionIndex)
        {
            var screen = EnsureMedicalScreen();
            screen.Table("m_factionsTable").SelectedRowIndex = factionIndex;
            screen.Button("m_selectFactionButton").PressButton();
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
        private TerminalScreen m_terminalScreen = new TerminalScreen();

        private MedicalsScreen m_medicalsScreen = new MedicalsScreen();

        public IMedicals Medicals => m_medicalsScreen;
        public ITerminal Terminal => m_terminalScreen;

        private MyGuiScreenBase ScreenWithFocus()
        {
            return MyScreenManager.GetScreenWithFocus();
        }

        public string FocusedScreen()
        {
            return MyScreenManager.GetScreenWithFocus().DisplayName();
        }

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
}

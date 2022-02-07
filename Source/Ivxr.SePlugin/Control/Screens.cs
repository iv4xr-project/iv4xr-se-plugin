using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;

namespace Iv4xr.SePlugin.Control
{
    public class Screens : IScreens, IMedicals
    {
        private MyGuiScreenBase ScreenWithFocus()
        {
            return MyScreenManager.GetScreenWithFocus();
        }

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

        private MyGuiScreenMedicals EnsureMedicalScreen()
        {
            return MyGuiScreenExtensions.EnsureFocusedScreen<MyGuiScreenMedicals>();
        }

        private List<MyGuiControlTable.Row> MedicalRoomRows()
        {
            return EnsureMedicalScreen().Table("m_respawnsTable").RowsAsList();
        }

        private List<MyGuiControlTable.Row> FactionRows()
        {
            return EnsureMedicalScreen().Table("m_factionsTable").RowsAsList();
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

        public IMedicals Medicals => this;
    }
}

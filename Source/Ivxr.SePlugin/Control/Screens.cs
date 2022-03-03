using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;

namespace Iv4xr.SePlugin.Control
{
    public class MedicalsScreen : IMedicals
    {
        public void Respawn(int roomIndex)
        {
            var screen = EnsureMedicalScreen();
            screen.Table("m_respawnsTable").SelectedRowIndex = roomIndex;
            screen.ClickButton("m_respawnButton");
        }

        public void ChooseFaction(int factionIndex)
        {
            var screen = EnsureMedicalScreen();
            screen.Table("m_factionsTable").SelectedRowIndex = factionIndex;
            screen.ClickButton("m_selectFactionButton");
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
        private TerminalScreen m_terminalScreen;

        private MedicalsScreen m_medicalsScreen = new MedicalsScreen();

        public Screens(GameSession gameSession, LowLevelObserver lowLevelObserver)
        {
            m_terminalScreen = new TerminalScreen(gameSession, lowLevelObserver);
        }

        public IMedicals Medicals => m_medicalsScreen;
        public ITerminal Terminal => m_terminalScreen;

        private MyGuiScreenBase ScreenWithFocus()
        {
            return MyScreenManager.GetScreenWithFocus();
        }

        [RunOutsideGameLoop]
        public string FocusedScreen()
        {
            return MyScreenManager.GetScreenWithFocus().DisplayName();
        }

        [RunOutsideGameLoop]
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

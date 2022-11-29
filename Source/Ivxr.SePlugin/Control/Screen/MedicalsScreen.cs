using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class MedicalsScreen : AbstractScreen<MyGuiScreenMedicals, MedicalsData>, IMedicals
    {
        public override MedicalsData Data()
        {
            return new MedicalsData()
            {
                MedicalRooms = MedicalRoomRows().Select(row => new MedicalRoomData()
                        {
                            Name = row.GetCell(0).Text.ToString(),
                            AvailableIn = row.GetCell(1).Text.ToString()
                        }
                ).ToList(),
                Factions = FactionRows().Select(row => new Faction()
                        {
                            Tag = row.GetCell(0).Text?.ToString() ?? "",
                            Name = row.GetCell(1).Text?.ToString() ?? "",
                        }
                ).ToList(),
                ShowFactions = Screen.GetInstanceFieldOrThrow<bool>("m_showFactions"),
                ShowMotD = Screen.GetInstanceFieldOrThrow<bool>("m_showMotD"),
                IsMotdOpen = Screen.GetInstanceFieldOrThrow<bool>("m_isMotdOpen"),
                Paused = Screen.GetInstanceFieldOrThrow<bool>("m_paused"),
                IsMultiplayerReady = Screen.GetInstanceFieldOrThrow<bool>("m_isMultiplayerReady"),
                RespawnButton = Screen.GetInstanceField<MyGuiControlBase>("m_respawnButton")?.ToControlBase(),
            };
        }

        public void SelectRespawn(int roomIndex)
        {
            var table = Screen.Table("m_respawnsTable");
            table.SelectedRowIndex = roomIndex.CheckIndex();
            Screen.CallMethod<object>("OnTableItemSelected", new object[] { table, new MyGuiControlTable.EventArgs() });
        }

        public void SelectFaction(int factionIndex)
        {
            var table = Screen.Table("m_factionsTable");
            table.SelectedRowIndex = factionIndex.CheckIndex();
            Screen.CallMethod<object>("OnFactionSelectClick", new object[] { null });
            Screen.CallMethod<object>("OnFactionsTableItemDoubleClick", new object[] { null, null });
        }

        public void Join()
        {
            Screen.ClickButton("m_selectFactionButton");
        }

        public void Respawn()
        {
            Screen.ClickButton("m_respawnButton");
        }

        public void ShowMessageOfTheDay()
        {
            Screen.ClickButton("m_MotdButton");
        }

        public void Refresh()
        {
            Screen.ClickButton("m_refreshButton");
        }

        private List<MyGuiControlTable.Row> MedicalRoomRows()
        {
            return Screen.TableOrNull("m_respawnsTable")?.RowsAsList() ?? new List<MyGuiControlTable.Row>();
        }

        private List<MyGuiControlTable.Row> FactionRows()
        {
            return Screen.TableOrNull("m_factionsTable")?.RowsAsList() ?? new List<MyGuiControlTable.Row>();
        }
    }
}

using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class MedicalsData
    {
        public List<MedicalRoom> MedicalRooms;
        public List<Faction> Factions;
        public bool ShowFactions;
        public bool ShowMotD;
        public bool IsMotdOpen;
        public bool Paused;
        public bool IsMultiplayerReady;
        public GuiControlBase RespawnButton;
    }
}

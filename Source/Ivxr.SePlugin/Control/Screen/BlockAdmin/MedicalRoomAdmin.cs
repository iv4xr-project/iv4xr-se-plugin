using Iv4xr.SpaceEngineers;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class MedicalRoomAdmin: AbstractBlockAdmin<MyMedicalRoom>, IMedicalRoomAdmin
    {
        public MedicalRoomAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
            
        }
        
        public void SetRespawnAllowed(string blockId, bool respawnAllowed)
        {
            BlockById(blockId).RespawnAllowed = respawnAllowed;
        }

        public void SetHealingAllowed(string blockId, bool healingAllowed)
        {
            BlockById(blockId).HealingAllowed = healingAllowed;
        }

        public void SetRefuelAllowed(string blockId, bool refuelAllowed)
        {
            BlockById(blockId).RefuelAllowed = refuelAllowed;
        }

        public void SetSpawnWithoutOxygenEnabled(string blockId, bool spawnWithoutOxygenEnabled)
        {
            BlockById(blockId).SpawnWithoutOxygenEnabled = spawnWithoutOxygenEnabled;
        }
    }
}

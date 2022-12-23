using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Cube;
using VRage.Sync;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class WarheadAdmin: AbstractBlockAdmin<MyWarhead>, IWarheadAdmin
    {
        public WarheadAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
            
        }
        
        public void Explode(string blockId)
        {
            BlockById(blockId).Explode();
        }
        
        public void Detonate(string blockId)
        {
            BlockById(blockId).Detonate();
        }

        public void SetCountdownMs(string blockId, int countdown)
        {
            BlockById(blockId).GetInstanceField<Sync<int, SyncDirection.BothWays>>("m_countdownMs").Value = countdown;
        }
        
        public bool StartCountdown(string blockId)
        {
            return BlockById(blockId).StartCountdown();
        }
        
        public bool StopCountdown(string blockId)
        {
            return BlockById(blockId).StopCountdown();
        }

        public void SetArmed(string blockId, bool armed)
        {
            BlockById(blockId).IsArmed = armed;
        }
    }
}

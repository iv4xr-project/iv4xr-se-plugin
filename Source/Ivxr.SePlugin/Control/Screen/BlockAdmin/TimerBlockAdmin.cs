using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class TimerBlockAdmin : AbstractBlockAdmin<MyTimerBlock>, ITimerBlockAdmin
    {
        public TimerBlockAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetTriggerDelay(string blockId, float triggerDelay)
        {
            BlockById(blockId).TriggerDelay = triggerDelay;
        }

        public void Start(string blockId)
        {
            BlockById(blockId).Start();
        }

        public void Stop(string blockId)
        {
            BlockById(blockId).Stop();
        }

        public void TriggerNow(string blockId)
        {
            BlockById(blockId).CallMethod<object>("Trigger");
        }
    }
}

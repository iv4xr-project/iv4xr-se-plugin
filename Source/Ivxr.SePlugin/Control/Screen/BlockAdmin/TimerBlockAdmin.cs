using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Cube;
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
    }
}

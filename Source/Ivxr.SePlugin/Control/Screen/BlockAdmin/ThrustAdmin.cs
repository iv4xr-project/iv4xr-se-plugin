using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class ThrustAdmin : AbstractBlockAdmin<MyThrust>, IThrustAdmin
    {
        public ThrustAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetThrustOverride(string blockId, float thrustOverride)
        {
            BlockById(blockId).ThrustOverride = thrustOverride;
        }
    }
}

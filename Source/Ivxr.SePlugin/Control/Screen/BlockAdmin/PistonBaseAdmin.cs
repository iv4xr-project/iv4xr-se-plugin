using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class PistonBaseAdmin : AbstractBlockAdmin<MyPistonBase>, IPistonBaseAdmin
    {
        public PistonBaseAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetVelocity(string blockId, float velocity)
        {
            BlockById(blockId).Velocity.Value = velocity;
        }

        public void RecreateTop(string blockId)
        {
            BlockById(blockId).CallMethod<object>("RecreateTop", new object[] { null, false, false });
        }
    }
}

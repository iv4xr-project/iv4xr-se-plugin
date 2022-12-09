using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities.Blocks;
using Sandbox.Game.Entities.Cube;

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

    }
}

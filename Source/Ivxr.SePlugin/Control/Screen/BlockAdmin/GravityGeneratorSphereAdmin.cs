using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Cube;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class GravityGeneratorSphereAdmin : AbstractBlockAdmin<MyGravityGeneratorSphere>, IGravityGeneratorSphereAdmin
    {
        public GravityGeneratorSphereAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetRadius(string blockId, float radius)
        {
            BlockById(blockId).Radius = radius;
        }
        
        public void SetGravityAcceleration(string blockId, float gravityAcceleration)
        {
            BlockById(blockId).GravityAcceleration = gravityAcceleration;
        }
    }
}

using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities.Cube;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class GravityGeneratorAdmin : AbstractBlockAdmin<MyGravityGenerator>, IGravityGeneratorAdmin
    {
        public GravityGeneratorAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetFieldSize(string blockId, PlainVec3D fieldSize)
        {
            BlockById(blockId).FieldSize = fieldSize.ToVector3();
        }

        public void SetGravityAcceleration(string blockId, float gravityAcceleration)
        {
            BlockById(blockId).GravityAcceleration = gravityAcceleration;
        }
    }
}

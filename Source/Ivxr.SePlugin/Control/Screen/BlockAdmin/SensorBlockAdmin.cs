using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities.Blocks;
using Sandbox.Game.Entities.Cube;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class SensorBlockAdmin : AbstractBlockAdmin<MySensorBlock>, ISensorBlockAdmin
    {
        public SensorBlockAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetFieldMin(string blockId, PlainVec3D fieldMin)
        {
            BlockById(blockId).FieldMin = fieldMin.ToVector3();
        }

        public void SetFieldMax(string blockId, PlainVec3D fieldMax)
        {
            BlockById(blockId).FieldMax = fieldMax.ToVector3();
        }
    }
}

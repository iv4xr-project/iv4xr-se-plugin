using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.PluginLib.Control
{
    public class ContinuousMovementContext
    {
        public PlainVec3D MoveVector = PlainVec3DConst.Zero;
        public PlainVec2F RotationVector = PlainVec2FConst.Zero;
        public float Roll = 0;
        public int TicksLeft = 0;

        public void UseTick()
        {
            TicksLeft -= 1;
        }

        public bool IsValid()
        {
            return (MoveVector.Length() > 0 || RotationVector.Length() > 0) && TicksLeft > 0;
        }

        public void Reset()
        {
            TicksLeft = 0;
            MoveVector = PlainVec3DConst.Zero;
            RotationVector = PlainVec2FConst.Zero;
        }
    }
}

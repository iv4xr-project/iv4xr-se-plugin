namespace Iv4xr.SePlugin.WorldModel
{
    public class Block : Entity
    {
        //public float GridSize;  // NOTE: pull up to Grid if we add grids.
        public float MaxIntegrity;
        public float BuildIntegrity;
        public float Integrity;

        public string BlockType;

        public PlainVec3D MinPosition;
        public PlainVec3D MaxPosition;
        public PlainVec3D Size;
        public PlainVec3D OrientationForward;
        public PlainVec3D OrientationUp;
    }
}


namespace Iv4xr.SpaceEngineers.WorldModel
{
    /// <summary>
    /// Yet another Vector3D class. This time without any properties for easy (JSON) serialization.
    /// </summary>
    public struct PlainVec3F
    {
        public float X;
        public float Y;
        public float Z;

        public PlainVec3F(float x, float y, float z)
        {
            X = x;
            Y = y;
            Z = z;
        }
    }
}

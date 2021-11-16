
namespace Iv4xr.SpaceEngineers.WorldModel
{
    /// <summary>
    /// Yet another Vector3I class. This time without any properties for easy (JSON) serialization.
    /// </summary>
    public struct PlainVec3I
    {
        public int X;
        public int Y;
        public int Z;

        public PlainVec3I(int x, int y, int z)
        {
            X = x;
            Y = y;
            Z = z;
        }
    }
}

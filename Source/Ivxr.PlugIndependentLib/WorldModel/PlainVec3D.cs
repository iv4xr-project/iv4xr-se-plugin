using System;

namespace Iv4xr.PluginLib.WorldModel
{
    /// <summary>
    /// Yet another Vector3D class. This time without any properties for easy (JSON) serialization.
    /// </summary>
    public struct PlainVec3D
    {
        public double X;
        public double Y;
        public double Z;

        public PlainVec3D(double x, double y, double z)
        {
            X = x;
            Y = y;
            Z = z;
        }

        public double Length()
        {
            return Math.Sqrt(X * X + Y * Y + Z * Z);
        }

        public static PlainVec3D Zero = new PlainVec3D(0, 0, 0);
    }
}

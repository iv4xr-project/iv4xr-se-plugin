
using System;

namespace Iv4xr.PluginLib.WorldModel
{
    public struct PlainVec2F
    {
        public float X;
        public float Y;

        public PlainVec2F(float x, float y)
        {
            X = x;
            Y = y;
        }
        
        public double Length()
        {
            return Math.Sqrt(X * X + Y * Y);
        }
        
        public static PlainVec2F Zero = new PlainVec2F(0, 0);
    }
}

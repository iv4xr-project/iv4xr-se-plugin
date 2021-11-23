
using System;

namespace Iv4xr.SpaceEngineers.WorldModel
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
    }
    
    // Put this into a separate class to allow serialization by LitJson (it can't handle recursive types)
    public static class PlainVec2FConst
    {
        public static PlainVec2F Zero = new PlainVec2F(0, 0);
    }
}

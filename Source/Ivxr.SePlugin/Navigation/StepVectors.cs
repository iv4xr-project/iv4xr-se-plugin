using System;
using System.Collections.Generic;
using VRageMath;

namespace Iv4xr.SePlugin.Navigation
{
    internal class StepVectors
    {
        private const int UpIdx = 0;
        private const int DownIdx = 1;
        private const int RightIdx = 2;
        private const int LeftIdx = 3;
        private const int FwdIdx = 4;
        private const int BackIdx = 5;

        public const int FirstSideIdx = RightIdx;
        public const int Count = 6;

        public Vector3I Right => Steps[RightIdx];
        public Vector3I Forward => Steps[FwdIdx];

        public readonly Vector3I[] Steps = new Vector3I[Count];
        
        public StepVectors(Vector3I up)
        {
            if (up.RectangularLength() != 1)
                throw new ArgumentException("Up must be a unit vector.");
            
            Steps[UpIdx] = up;
            Steps[DownIdx] = -up;

            // We don't care which side is actually forward, we just don't want to travel up or down.
            // Maybe there is some clever math how to do this transformation.
            if (up.Z == 0)
            {
                Steps[FwdIdx] = Vector3I.Forward;

                Steps[RightIdx] = (up.X == 0) ? Vector3I.Right : Vector3I.Up;
            }
            else // Z != 0  -> the other two dimensions should be "free" to use for side directions
            {
                if (up.X != 0 || up.Y != 0)
                    throw new ArgumentException("X and Y of the Up vector expected to be 0.");
                
                Steps[FwdIdx] = Vector3I.Up;
                Steps[RightIdx] = Vector3I.Right;
            }

            Steps[LeftIdx] = -Right;
            Steps[BackIdx] = -Forward;
        }
        
        public IEnumerable<Vector3I> Enumerate(int startIndex = 0)
        {
            for (var i = startIndex; i < Count; i++)
                yield return Steps[i];
        }
 
        public IEnumerable<Vector3I> EnumerateSides()
        {
            return Enumerate(startIndex: FirstSideIdx);
        }
    }

}

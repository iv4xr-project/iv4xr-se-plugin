using Iv4xr.SePlugin.Navigation;
using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
    public class StepVectorsTests
    {
        [Fact]
        public void StepVectorsAreCorrect()
        {
            CheckStepVectors(Vector3I.Up);
            CheckStepVectors(Vector3I.Left);
            CheckStepVectors(Vector3I.Forward);
            
            foreach (var direction in new StepVectors(Vector3I.Up).Enumerate())
                CheckStepVectors(direction);
        }

        private static void CheckStepVectors(Vector3I up)
        {
            var stepVectors = new StepVectors(up);

            // Sides are perpendicular to the Up vector (dot product is zero)
            foreach (var side in stepVectors.EnumerateSides())
            {
                Assert.Equal(0, Vector3I.Dot(up, side));
            }

            // All directions are unit vectors and they are different from each other.
            for (int i = 0; i < StepVectors.Count; i++)
            {
                var step = stepVectors.Steps[i];

                Assert.Equal(1, step.RectangularLength());

                for (int j = 0; j < StepVectors.Count; j++)  // Check all are different.
                    if (j != i)
                        Assert.False(step.Equals(stepVectors.Steps[j]));
            }
        }
    }
}

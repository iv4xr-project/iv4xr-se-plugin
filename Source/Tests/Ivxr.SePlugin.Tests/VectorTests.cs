using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
    public class VectorTests
    {
        [Fact]
        public void VectorToMatrix()
        {
            var matrix = MatrixD.Identity;
            Assert.Equal(matrix.Forward, Vector3D.Forward);
            Assert.Equal(matrix.Up, Vector3D.Up);

            Assert.Equal(matrix.Forward, new Vector3D(0, 0, -1));
            Assert.Equal(matrix.Up, new Vector3D(0, 1, 0));

            Assert.Equal(matrix.Backward, new Vector3D(0, 0, 1));
            Assert.Equal(matrix.Down, new Vector3D(0, -1, 0));
            Assert.Equal(matrix.Left, new Vector3D(-1, 0, 0));
            Assert.Equal(matrix.Right, new Vector3D(1, 0, 0));
        }
        
        [Fact]
        public void RandomValuesRotate()
        {
            var matrix = MatrixD.Identity;
            Assert.Equal(matrix.Forward, Vector3D.Forward);
            Assert.Equal(matrix.Up, Vector3D.Up);

            Assert.Equal(matrix.Forward, new Vector3D(0, 0, -1));
            Assert.Equal(matrix.Up, new Vector3D(0, 1, 0));

            Assert.Equal(matrix.Backward, new Vector3D(0, 0, 1));
            Assert.Equal(matrix.Down, new Vector3D(0, -1, 0));
            Assert.Equal(matrix.Left, new Vector3D(-1, 0, 0));
            Assert.Equal(matrix.Right, new Vector3D(1, 0, 0));
        }
    }
}

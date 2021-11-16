using Iv4xr.SePlugin;
using Iv4xr.SePlugin.Json;
using Iv4xr.SpaceEngineers.WorldModel;
using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
    public class PlainVec3DTests
    {
        [Fact]
        public void ConvertsCorrectlyFromVector3D()
        {
            var vector3D = new Vector3D(1, 2, 3);

            var plainVec = new PlainVec3D(1, 2, 3);
            var convertedPlainVec = vector3D.ToPlain();

            var jsoner = new Jsoner();

            Assert.Equal(jsoner.ToJson(plainVec), jsoner.ToJson(convertedPlainVec));
        }
    }
}

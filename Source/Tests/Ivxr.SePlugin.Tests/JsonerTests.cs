using Iv4xr.PluginLib.Json;
using Iv4xr.SePlugin.Json;
using Iv4xr.PluginLib.WorldModel;
using VRageMath;
using Xunit;
using Iv4xr.SePlugin.Config;
using Iv4xr.SePlugin.Control;

namespace Ivxr.SeGameLib.Tests
{
    public class JsonerSeTests
    {
        private readonly IJsoner m_jsoner = new Jsoner();

        private const int Precision = 4;
        
        [Fact]
        public void DeserializesPlainVec3DIntoVector3D()
        {
            var originalVector = new PlainVec3D(1, 2, -3);

            var json = m_jsoner.ToJson(originalVector);

            var vector3 = m_jsoner.ToObject<Vector3D>(json);

            Assert.Equal(originalVector.X, vector3.X);
            Assert.Equal(originalVector.Y, vector3.Y);
            Assert.Equal(originalVector.Z, vector3.Z);
        }

        [Fact]
        public void JsonerKeepsDefaultValues()
        {
            const string configJson = "{\"JsonRpcPort\":3333}";

            var config = m_jsoner.ToObject<PluginConfig>(configJson);
            
            Assert.Equal(Observer.DefaultRadius, config.ObservationRadius);
        }

        /* (don't even show this in the list)
        [Fact(Skip="One time use.")]
        public void MeasureCommandPrefixLength()
        {
            string commandPrefix = "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\"";

            Assert.Equal(36, commandPrefix.Length);  // Need this constant in the code.
        }
        */
    }
}

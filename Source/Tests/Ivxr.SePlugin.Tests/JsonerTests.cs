using Iv4xr.SePlugin.Json;
using System;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;
using Xunit;
using Iv4xr.PluginLib.Comm;
using Iv4xr.SePlugin.Config;
using Iv4xr.SePlugin.Control;

namespace Ivxr.SeGameLib.Tests
{
    public class JsonerTests
    {
        private readonly Jsoner m_jsoner = new Jsoner();

        private const int Precision = 4;

        private class Class
        {
            public int Number = 9;
            public string Message = "Dragon lands.";
        }

        [Fact]
        public void ConvertsObjectToJson()
        {
            var obj = new Class();

            Assert.Equal("{\"Number\":9,\"Message\":\"Dragon lands.\"}", m_jsoner.ToJson(obj));
        }

        [Fact]
        public void ConvertsSeObservationToJson()
        {
            var observation = new Observation
            {
                    Id = "Foo",
                    Position = new PlainVec3D(1, 2, 3)
            };

            var json = m_jsoner.ToJson(observation);

            Assert.Equal("{\"AgentID\":\"Foo\"," +
                         "\"Position\":{\"X\":1.0,\"Y\":2.0,\"Z\":3.0}," +
                         "\"OrientationForward\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"OrientationUp\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"Velocity\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"Extent\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"Entities\":[]," +
                         "\"Grids\":null}",
                json);
        }

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

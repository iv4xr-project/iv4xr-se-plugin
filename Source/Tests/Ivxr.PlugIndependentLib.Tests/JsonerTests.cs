using Iv4xr.PluginLib.Json;
using Iv4xr.PluginLib.WorldModel;
using Iv4xr.SePlugin.Config;
using Xunit;

namespace Ivxr.PlugIndependentLib.Tests
{
    public class JsonerTests
    {
        private readonly IJsoner m_jsoner = new NewtonJsoner();

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
            var observation = new CharacterObservation()
            {
                Id = "Foo",
                Position = new PlainVec3D(1, 2, 3),
                HelmetEnabled = true,
                Health = (float)0.5,
            };

            var json = m_jsoner.ToJson(observation);
            // File.WriteAllText("observation.json", json);

            Assert.Equal("{\"Velocity\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"Extent\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"Camera\":null,\"JetpackRunning\":false,\"HelmetEnabled\":true,\"Health\":0.5,\"Oxygen\":0.0," +
                         "\"Hydrogen\":0.0,\"SuitEnergy\":0.0," +
                         "\"HeadLocalXAngle\":0.0,\"HeadLocalYAngle\":0.0,\"TargetBlock\":null,\"TargetUseObject\":null," +
                         "\"Movement\":0,\"Inventory\":null,\"BootsState\":0,\"Id\":\"Foo\"," +
                         "\"Position\":{\"X\":1.0,\"Y\":2.0,\"Z\":3.0}," +
                         "\"OrientationForward\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
                         "\"OrientationUp\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}}",
                json);
        }


        [Fact]
        public void JsonerKeepsDefaultValues()
        {
            const string configJson = "{\"JsonRpcPort\":3333}";

            var config = m_jsoner.ToObject<PluginConfig>(configJson);
            
            Assert.Equal(PluginConfig.DEFAULT_RADIUS, config.ObservationRadius);
        }
    }
}

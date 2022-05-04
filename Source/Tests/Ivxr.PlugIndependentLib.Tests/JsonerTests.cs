using Iv4xr.PluginLib.Json;
using Iv4xr.SpaceEngineers.WorldModel;
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
        public void JsonerKeepsDefaultValues()
        {
            const string configJson = "{\"JsonRpcPort\":3333}";

            var config = m_jsoner.ToObject<PluginConfig>(configJson);

            Assert.Equal(PluginConfigDefaults.RADIUS, config.ObservationRadius);
        }
    }
}

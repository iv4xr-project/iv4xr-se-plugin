using System.ComponentModel;
using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Json;
using Iv4xr.SePlugin.Config;
using VRage.Plugins;

namespace Iv4xr.SePlugin
{
    public class IvxrPluginConfiguration : IPluginConfiguration
    {
        [Category("Network")] 
        public readonly string Hostname;
        [Category("Network")]
        public readonly int Port;
        [Category("Observation")] 
        public readonly double ObservationRadius;

        private readonly ILog m_log;

        public IvxrPluginConfiguration(PluginConfig config, ILog log)
        {
            Hostname = config.Hostname;
            Port = config.JsonRpcPort;
            ObservationRadius = config.ObservationRadius;
            m_log = log;
        }

        public void Save(string userDataPath)
        {
            var configPath = Path.Combine(userDataPath, ConfigLoader.CONFIG_FILE);
            new ConfigLoader(m_log, new NewtonJsoner(), configPath).Save(ToPluginConfig());
        }

        public PluginConfig ToPluginConfig()
        {
            return new PluginConfig()
            {
                Hostname = Hostname,
                JsonRpcPort = Port,
                ObservationRadius = ObservationRadius,
            };
        }
    }
}

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
        [Category("Network")] public string Hostname = PluginConfigDefaults.HOSTNAME;
        [Category("Network")] public int Port = PluginConfigDefaults.PORT;
        [Category("Observation")] public double ObservationRadius = PluginConfigDefaults.RADIUS;

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
            var configPath = Path.Combine(userDataPath, PluginConfigDefaults.CONFIG_FILE);
            new ConfigLoader(m_log, new NewtonJsoner(), configPath).Save(ToPluginConfig());
        }

        private PluginConfig ToPluginConfig()
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

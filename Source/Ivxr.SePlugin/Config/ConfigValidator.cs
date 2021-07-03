using System;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;

namespace Iv4xr.SePlugin.Config
{
    public class ConfigValidator
    {
        // This can't be public because it would cause infinite recursion in serialization.
        private static readonly PluginConfig Default = new PluginConfig();

        private readonly ILog m_log;

        public ConfigValidator(ILog log)
        {
            m_log = log;
        }

        /// <summary>
        /// Any invalid values are set to defaults. Errors are logged.
        /// </summary>
        public void EnforceValidConfig(PluginConfig config)
        {
            config.JsonRpcPort = EnforceValidPort(config.JsonRpcPort, Default.JsonRpcPort, "JsonRpcPort");
            
            EnforceValidObservationRadius(config);
        }
        
        private void EnforceValidObservationRadius(PluginConfig config)
        {
            try
            {
                Observer.ValidateRadius(config.ObservationRadius);
            }
            catch (Exception e)
            {
                m_log.WriteLine(e.Message + " Using the default value.");
                config.ObservationRadius = Default.ObservationRadius;
            }
        }

        private int EnforceValidPort(int port, int defaultPort, string name)
        {
            if (port >= 1 && port <= 65_535)
                return port;
            
            m_log.WriteLine($"{name}: Port number {port} is out of the valid range [1, 65_535]."
                           + $" Using the default value {defaultPort}.");
            
            return defaultPort;
        }
    }
}

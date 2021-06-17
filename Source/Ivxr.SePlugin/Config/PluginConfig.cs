using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Control;

namespace Iv4xr.SePlugin.Config
{
    public class PluginConfig
    {
        public int Port = PluginServer.DefaultPort;
        public int JsonRpcPort = JsonRpcStarter.DefaultPort;
        public double ObservationRadius = Observer.DefaultRadius;
    }
}

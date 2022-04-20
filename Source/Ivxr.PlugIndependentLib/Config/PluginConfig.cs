namespace Iv4xr.SePlugin.Config
{
    // This class must not contain any constants, only the config items, because it is used to generate the config file.
    public class PluginConfig
    {
        public string Hostname = PluginConfigDefaults.HOSTNAME;
        public int JsonRpcPort = PluginConfigDefaults.PORT;
        public double ObservationRadius = PluginConfigDefaults.RADIUS;
    }

    public static class PluginConfigDefaults
    {
        public const string HOSTNAME = "127.0.0.1";
        public const double RADIUS = 25.0d;
        public const int PORT = 3333;
    }
}

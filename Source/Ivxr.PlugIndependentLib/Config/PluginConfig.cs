﻿namespace Iv4xr.SePlugin.Config
{
    public class PluginConfig
    {
        public int JsonRpcPort = DEFAULT_PORT;
        public double ObservationRadius = DEFAULT_RADIUS;
        
        public const double DEFAULT_RADIUS = 25.0d;
        public const int DEFAULT_PORT = 3333;
    }
}
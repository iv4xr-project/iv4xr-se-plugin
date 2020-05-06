using System;
using System.Net;
using System.Net.Sockets;

using EU.Iv4xr.PluginLib;

namespace SeServerMock
{
    // ReSharper disable once ClassNeverInstantiated.Global
    class Program
    {
        public static void Main(string[] args)
        {
            var server = new PluginServer();
            server.Start(waitForFinish: true);
        }
    }
}
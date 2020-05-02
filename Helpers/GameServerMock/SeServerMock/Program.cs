using System;
using System.Net;
using System.Net.Sockets;

namespace SeServerMock
{
    // ReSharper disable once ClassNeverInstantiated.Global
    class Program
    {
        public static void Main(string[] args)
        {
            var server = new Server();
            server.Start();
        }
    }
}
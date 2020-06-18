using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using EU.Iv4xr.PluginLib;
using EU.Iv4xr.PluginLib.Log;

namespace SeServerMock
{
    // ReSharper disable once ClassNeverInstantiated.Global
    class Program
    {
        public static void Main(string[] args)
        {
            var log = new ConsoleLog();
            using (var requestQueue = new RequestQueue())
            {

                var server = new PluginServer(log, requestQueue);
                server.Start();

                while (true)
                {
                    while (requestQueue.Requests.TryDequeue(out Request request))
                    {
                        log.WriteLine("dequeued: " + request.Message);

                        requestQueue.Replys.Add(
                            new Request(request.ClientStream, $"Got {request.Message.Length} bytes, thanks!"));
                    }

                    Thread.Sleep(50);
                }

                // server.Start(waitForFinish: true);
            }
        }
    }
}
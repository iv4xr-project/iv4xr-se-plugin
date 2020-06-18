﻿using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Log;
using Iv4xr.SeGameLib.Control;
using SeServerMock.Mocks;

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

				var observer = new MockObserver();

                var controller = new Controller(requestQueue, observer);

				while (true)
                {
					controller.ProcessRequests();

					/*
                    while (requestQueue.Requests.TryDequeue(out Request request))
                    {
                        log.WriteLine("dequeued: " + request.Message);

                        requestQueue.Replies.Add(
                            new Request(request.ClientStream, $"Got {request.Message.Length} bytes, thanks!"));
                    }
					*/

                    Thread.Sleep(50);
                }

                // server.Start(waitForFinish: true);
            }
        }
    }
}
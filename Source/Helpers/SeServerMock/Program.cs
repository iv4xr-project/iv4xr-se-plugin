﻿using System.Threading;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Log;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Control;
using SeServerMock.Mocks;

namespace SeServerMock
{
    // ReSharper disable once ClassNeverInstantiated.Global
    class Program
    {
        public static void Main(string[] args)
        {
            var log = new ConsoleLog();
            var sessionController = new MockSessionController() {Log = log};

            var se = new RealSpaceEngineers(
                new MockObserver() {Log = log},
                new MockCharacterController() {Log = log},
                sessionController,
                new MockItems() {Log = log},
                new MockDefinitions() {Log = log}
            );

            RunOldDispatcher(se, log);
            //RunJsonRpc(se, log);
        }

        private static void RunOldDispatcher(ISpaceEngineers se, ILog log)
        {
            using (var requestQueue = new RequestQueue())
            {
                var dispatcher = new Dispatcher(requestQueue, se) {Log = log};
                while (true)
                {
                    dispatcher.ProcessRequests();

                    /*
                    while (requestQueue.Requests.TryDequeue(out Request request))
                    {
                        log.WriteLine("dequeued: " + request.Message);
    
                        requestQueue.Replies.Add(
                            new Request(request.ClientStream, $"Got {request.Message.Length} bytes, thanks!"));
                    }
                    */

                    Thread.Sleep(5);
                }

                // server.Start(waitForFinish: true);
            }
        }
    }
}

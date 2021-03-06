﻿using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;

namespace Iv4xr.SePlugin.Communication
{
    public class JsonRpcStarter
    {
        public ILog Log { get; set; }

        public const int DefaultPort = 3333;

        private readonly string m_hostname;
        private readonly int m_port;

        private static AustinJsonRpcSpaceEngineers service;

        private readonly ISpaceEngineers m_se;

        public JsonRpcStarter(ISpaceEngineers se, string hostname = "127.0.0.1", int port = DefaultPort)
        {
            m_se = se;
            m_hostname = hostname;
            m_port = port;
            service = new AustinJsonRpcSpaceEngineers(se);
        }

        public void Start()
        {
            var thread = new Thread(StartSync)
            {
                IsBackground = true,
                Name = "Iv4xr plugin json-rpc server thread"
            };
            
            thread.Start();
        }

        private void StartSync()
        {
            var task = StartAsync();
        }

        private async Task StartAsync()
        {
            var server = new TcpListener(IPAddress.Parse(m_hostname), m_port);
            server.Start();
            Log.WriteLine(("Server started at port " + m_port));
            while (true)
            {
                Log.WriteLine(("Waiting for connection"));
                try
                {
                    using (var client = await server.AcceptTcpClientAsync())
                    using (var stream = client.GetStream())
                    {
                        Log.WriteLine("Client Connected..");
                        await HandleRequestAsync(stream);
                    }
                }
                catch (Exception e)
                {
                    Log.WriteLine("RPCServer exception " + e);
                }
            }
        }

        private async Task HandleRequestAsync(Stream stream)
        {

            var reader = new StreamReader(stream, Encoding.UTF8);
            var writer = new StreamWriter(stream, new UTF8Encoding(false));
            Log.WriteLine($"JSON-RPC listener attached. Waiting for requests...");
            while (!reader.EndOfStream)
            {
                var line = reader.ReadLine();
                handleRequest(writer, line);
            }
            Log.WriteLine($"Connection terminated.");
        }

        private void handleRequest(StreamWriter writer, string line)
        {
            var rpcResultHandler = new AsyncCallback(
                state =>
                {
                    var async_ = ((JsonRpcStateAsync)state);
                    var result = async_.Result;
                    var writer_ = ((StreamWriter)async_.AsyncState);

                    writer_.WriteLine(result);
                    writer_.FlushAsync();
                });
            var async = new JsonRpcStateAsync(rpcResultHandler, writer) { JsonRpc = line };
            JsonRpcProcessor.Process(async, writer);
        }

    }
}

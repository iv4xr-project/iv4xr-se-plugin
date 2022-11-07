using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SePlugin.Config;
using Iv4xr.SpaceEngineers;

namespace Iv4xr.SePlugin.Communication
{
    public class JsonRpcStarter
    {
        public ILog Log { get; set; }

        private readonly string m_hostname;
        private readonly int m_port;

        private readonly AustinJsonRpcSpaceEngineers m_service;

        public JsonRpcStarter(
            ISpaceEngineers se,
            string hostname = PluginConfigDefaults.HOSTNAME,
            int port = PluginConfigDefaults.PORT
        )
        {
            m_hostname = hostname;
            m_port = port;
            m_service = new AustinJsonRpcSpaceEngineers(se);
            m_service.Bind();
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
            writer.AutoFlush = true;
            Log.WriteLine($"JSON-RPC listener attached. Waiting for requests...");
            while (!reader.EndOfStream)
            {
                var line = reader.ReadLine();
                writer.WriteLine(HandleString(line));
                writer.Flush();
            }
            Log.WriteLine($"Connection terminated.");
        }

        private string HandleString(string line)
        {
            return JsonRpcProcessor.ProcessSync(Handler.DefaultSessionId(), line, null);
        }
    }
}

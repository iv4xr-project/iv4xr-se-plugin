using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using StreamJsonRpc;

namespace Iv4xr.SePlugin.Communication
{
    public class JsonRpcStarter
    {
        public ILog Log { get; set; }

        private readonly ISpaceEngineers m_se;

        public JsonRpcStarter(ISpaceEngineers se)
        {
            m_se = se;
        }

        public void Start()
        {
            var thread = new Thread(StartSync)
            {
                IsBackground = true,
                Name = "Ivrx plugin json-rpc server thread"
            };
            thread.Start();
        }

        public void StartSync()
        {
            var task = StartAsync();
        }

        private async Task StartAsync(string hostname = "127.0.0.1", int listenPort = 3333)
        {
            var server = new TcpListener(IPAddress.Parse(hostname), listenPort);
            server.Start();
            Log.WriteLine(("Server started at port " + listenPort));
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
            var jsonRpc = Create(stream, stream);
            jsonRpc.StartListening();
            Log.WriteLine($"JSON-RPC listener attached. Waiting for requests...");
            await jsonRpc.Completion;
            Log.WriteLine($"Connection terminated.");
        }

        private JsonRpc Create(Stream writer, Stream reader)
        {
            Log.WriteLine(
                $"Connection request received. Spinning off an async Task to cater to requests.");
            var jsonRpcMessageHandler =
                    new NewLineDelimitedMessageHandler(writer, reader, new JsonMessageFormatter(Encoding.UTF8));
            var jsonRpc = new JsonRpc(jsonRpcMessageHandler);
            AddLocalRpcTargets(jsonRpc);
            return jsonRpc;
        }

        private void AddLocalRpcTargets(JsonRpc jsonRpc)
        {
            jsonRpc.AddLocalRpcTarget(m_se.Character, "Character.");
            jsonRpc.AddLocalRpcTarget(m_se.Session, "Session.");
            jsonRpc.AddLocalRpcTarget(m_se.Items, "Items.");
            jsonRpc.AddLocalRpcTarget(m_se.Observer, "Observer.");
            jsonRpc.AddLocalRpcTarget(m_se.Definitions, "Definitions.");
        }
    }

    public static class JsonRpcExtensions
    {
        public static void AddLocalRpcTarget<TService>(this JsonRpc jsonRpc, TService service, string prefix)
        {
            jsonRpc.AddLocalRpcTarget<TService>(service, new JsonRpcTargetOptions()
            {
                MethodNameTransform = CommonMethodNameTransforms.Prepend(prefix),
                DisposeOnDisconnect = true
            });
        }
    }
}

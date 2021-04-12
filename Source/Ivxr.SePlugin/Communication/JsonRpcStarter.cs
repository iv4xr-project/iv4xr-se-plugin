using System.Threading;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;

namespace Iv4xr.SePlugin.Communication
{
    public class JsonRpcStarter
    {
        private RequestQueue m_jsonRpcRequestQueue;
        private DispatcherContext m_dispatcherContext;

        public JsonRpcStarter(RequestQueue jsonRpcRequestQueue, DispatcherContext dispatcherContext)
        {
            m_jsonRpcRequestQueue = jsonRpcRequestQueue;
            m_dispatcherContext = dispatcherContext;
        }

        //not sure how exactly this works, but library must somehow screen through existing objects and add them to rpc handling
        //not a big fan, since this feels "global" and there's less control
        private object _svc;

        public void Start()
        {
            var thread = new Thread(StartSync)
            {
                IsBackground = true,
                Name = "Ivrx plugin json-rpc server thread"
            };
            thread.Start();
        }

        private void StartSync()
        {
            // must new up an instance of the service so it can be registered to handle requests.
            _svc = new IvxrJsonRpcService(m_dispatcherContext);
            SocketListener.Start(3333, async (writer, line) =>
            {
                if (line.Contains("Session."))
                {
                    var response = await JsonRpcProcessor.Process(line);
                    await writer.WriteLineAsync(response);
                    await writer.FlushAsync();
                }
                else
                {
                    m_jsonRpcRequestQueue.Requests.Enqueue(new RequestItem(writer, line));
                }
            });
        }
    }
}
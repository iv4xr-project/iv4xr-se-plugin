using System;
using System.Threading;
using AustinHarris.JsonRpc;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.SeLib;
using Iv4xr.SePlugin.Session;

namespace Iv4xr.SePlugin
{
    public class IvxrPluginContext : IDisposable
    {
        public ILog Log { get; private set; }
        public readonly Dispatcher Dispatcher;
        public readonly JsonRpcDispatcher JsonRpcDispatcher;
        public readonly JsonRpcStarter JsonRpcStarter;

        private readonly RequestQueue m_requestQueue = new RequestQueue();
        private readonly RequestQueue m_jsonRpcRequestQueue = new RequestQueue();

        private readonly PluginServer m_server;

        private readonly GameSession m_gameSession = new GameSession();

        public IvxrPluginContext()
        {
            var seLog = new SeLog(alwaysFlush: true);
            seLog.Init("ivxr-plugin.log");
            Log = seLog;

            var sessionController = new SessionController() {Log = Log};
            var sessionDispatcher = new SessionDispatcher(sessionController) {Log = Log};

            m_server = new PluginServer(Log, sessionDispatcher, m_requestQueue);
            var lowLevelObserver = new LowLevelObserver(m_gameSession) {Log = Log};
            var observer = new Observer(lowLevelObserver) {Log = Log};
            var controller = new CharacterController(m_gameSession);

            Dispatcher = new Dispatcher(m_requestQueue, observer, controller) {Log = Log};
            JsonRpcDispatcher = new JsonRpcDispatcher(m_jsonRpcRequestQueue) {Log = Log};
            JsonRpcStarter = new JsonRpcStarter(m_jsonRpcRequestQueue, observer, controller, sessionController)
                    {Log = Log};
        }


        public void StartServer()
        {
            m_server.Start();
            JsonRpcStarter.Start();
        }

        public void InitSession()
        {
            m_gameSession.InitSession();
        }

        public void EndSession()
        {
            Log.WriteLine("Ending session.");

            m_gameSession.EndSession();
        }

        protected virtual void Dispose(bool disposing)
        {
            if (alreadyDisposed)
                return;

            if (disposing)
            {
                // dispose managed state (managed objects).
                m_server.Stop();
                m_requestQueue.Dispose();
            }

            // TODO: Set large fields to null.

            alreadyDisposed = true;
        }

        #region The rest of IDisposable Support

        private bool alreadyDisposed = false; // To detect redundant calls

        // TODO: override a finalizer only if Dispose(bool disposing) above has code to free unmanaged resources.
        // ~PluginMain() {
        //   // Do not change this code. Put cleanup code in Dispose(bool disposing) above.
        //   Dispose(false);
        // }

        // This code added to correctly implement the disposable pattern.
        public void Dispose()
        {
            // Do not change this code. Put cleanup code in Dispose(bool disposing) above.
            Dispose(true);
            // TODO: uncomment the following line if the finalizer is overridden above.
            // GC.SuppressFinalize(this);
        }

        #endregion
    }

    public class JsonRpcStarter
    {
        public ILog Log { get; set; }
        private IObserver m_observer;
        private CharacterController m_characterController;
        private ISessionController m_sessionController;
        private RequestQueue m_jsonRpcRequestQueue;

        public JsonRpcStarter(RequestQueue jsonRpcRequestQueue, IObserver observer,
            ICharacterController characterController,
            SessionController sessionController)
        {
            m_jsonRpcRequestQueue = jsonRpcRequestQueue;
            m_observer = observer;
            m_characterController = characterController as CharacterController;
            m_sessionController = sessionController;
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

        public void StartSync()
        {
            // must new up an instance of the service so it can be registered to handle requests.
            _svc = new Iv4xrJsonRpcService(m_observer, m_characterController, m_sessionController);
            SocketListener.start(3333, async (writer, line) =>
            {
                if (line.Contains("LoadScenario"))
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
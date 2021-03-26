using Iv4xr.PluginLib;
using Iv4xr.SePlugin.SeLib;
using System;
using System.Collections.Generic;
using System.Text;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Log;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Session;
using Iv4xr.SePlugin.Control;
using AustinHarris.JsonRpc;
using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using Iv4xr.SePlugin.WorldModel;

namespace Iv4xr.SePlugin
{
    public class IvxrPluginContext : IDisposable
    {
        public ILog Log { get; private set; }
        public readonly Dispatcher Dispatcher;

        private readonly RequestQueue m_requestQueue = new RequestQueue();

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
            
            new Thread(() => 
            {
                Thread.CurrentThread.IsBackground = true; 
                StartJsonRpcService(observer, controller, sessionController); 
            }).Start();
        }

        //honestly don't understand this piece and how is this object added to handler
        private object _svc;
        
        void StartJsonRpcService(IObserver observer, ICharacterController characterController, SessionController sessionController)
        {
            // must new up an instance of the service so it can be registered to handle requests.
            _svc = new Iv4xrJsonRpcService(observer, characterController, sessionController);

            var rpcResultHandler = new AsyncCallback(
                state =>
                {
                    var async = ((JsonRpcStateAsync)state);
                    var result = async.Result;
                    var writer = ((StreamWriter)async.AsyncState);

                    writer.WriteLine(result);
                    writer.FlushAsync();
                });

            SocketListener.start(3333, (writer, line) =>
            {
                var async = new JsonRpcStateAsync(rpcResultHandler, writer) { JsonRpc = line };
                JsonRpcProcessor.Process(async, writer);
            });
        }

        public void StartServer()
        {
            m_server.Start();
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
}
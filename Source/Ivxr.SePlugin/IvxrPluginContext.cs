using System;
using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Config;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Json;
using Iv4xr.SePlugin.SeLib;
using VRage.FileSystem;

namespace Iv4xr.SePlugin
{
    public class IvxrPluginContext : IDisposable
    {
        public ILog Log { get; private set; }
        public readonly JsonRpcStarter JsonRpcStarter;
        public readonly FuncActionDispatcher FuncActionDispatcher;
        private const string CONFIG_FILE = "ivxr-plugin.config";

        private readonly GameSession m_gameSession = new GameSession();

        public IvxrPluginContext()
        {
            var seLog = new SeLog(alwaysFlush: true);
            seLog.Init("ivxr-plugin.log");
            Log = seLog;

            var configPath = Path.Combine(MyFileSystem.UserDataPath, CONFIG_FILE);
            var configLoader = new ConfigLoader(Log, new Jsoner(), configPath);
            var config = configLoader.LoadOrSaveDefault();

            var se = new RealSpaceEngineers(m_gameSession, Log, config);
            
            FuncActionDispatcher = new FuncActionDispatcher();
            
            JsonRpcStarter = new JsonRpcStarter(
                new SynchronizedSpaceEngineers(se, FuncActionDispatcher),
                port: config.JsonRpcPort
                ) {Log = Log};
        }


        public void StartServer()
        {
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

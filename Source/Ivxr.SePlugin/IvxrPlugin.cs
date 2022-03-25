using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Json;
using Iv4xr.SePlugin.Config;
using VRage.FileSystem;
using VRage.Plugins;

namespace Iv4xr.SePlugin
{
    public class IvxrPlugin : IConfigurablePlugin
    {
        public static IvxrPluginContext Context { get; private set; }

        // Shortcuts
        public static ILog Log { get; private set; }

        public void Init(object gameInstance)
        {
            if (Context != null)
            {
                Log.WriteLine("Init already called.");
                return;
            }
            var config = GetConfiguration(MyFileSystem.UserDataPath) as IvxrPluginConfiguration;

            Context = new IvxrPluginContext(config.ToPluginConfig());

            Log = Context.Log;
            Log.WriteLine($"{nameof(IvxrPlugin)} initialization finished.");

            Context.StartServer();
        }

        public void Update()
        {
            //m_log.WriteLine("Update called.");
        }


        protected virtual void Dispose(bool disposing)
        {
            if (!alreadyDisposed)
            {
                if (disposing)
                {
                    // dispose managed state (managed objects).
                    Context.Dispose();
                }

                // TODO: Set large fields to null.

                alreadyDisposed = true;
            }
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

        public string GetPluginTitle()
        {
            return "IV4XR";
        }

        public IPluginConfiguration GetConfiguration(string userDataPath)
        {
            var configPath = Path.Combine(userDataPath, ConfigLoader.CONFIG_FILE);
            var config = new ConfigLoader(Log, new NewtonJsoner(), configPath).LoadOrSaveDefault();
            return new IvxrPluginConfiguration(config, Log);
        }
    }
}

using System;

using VRage.Plugins;
using VRage.Utils;

namespace EU.Iv4xr.SePlugin
{
    public class IvxrPlugin : IPlugin
    {
        MyLog m_log;

        public void Init(object gameInstance)
        {
            m_log = new MyLog(alwaysFlush: true);
            m_log.Init("ivxr-plugin.log", new System.Text.StringBuilder("0.1.0"));
            m_log.WriteLine($"{nameof(IvxrPlugin)} initialization started.");
        }

        public void Update()
        {
        }

        #region IDisposable Support
        private bool disposedValue = false; // To detect redundant calls

        protected virtual void Dispose(bool disposing)
        {
            if (!disposedValue)
            {
                if (disposing)
                {
                    // TODO: dispose managed state (managed objects).
                }

                // TODO: free unmanaged resources (unmanaged objects) and override a finalizer below.
                // TODO: set large fields to null.

                disposedValue = true;
            }
        }

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

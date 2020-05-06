using EU.Iv4xr.PluginLib;
using EU.Iv4xr.SePlugin.SeLib;
using System;
using VRage.Game.ObjectBuilders.Gui;
using VRage.Plugins;
using VRage.Utils;

namespace EU.Iv4xr.SePlugin
{
	public class IvxrPlugin : IPlugin
	{
		public static ILog Log { get; private set; }

		private PluginServer m_server;

		static IvxrPlugin()
		{
			var seLog = new SeLog(alwaysFlush: true);
			seLog.Init("ivxr-plugin.log");

			Log = seLog;
		}

		public void Init(object gameInstance)
		{
			Log.WriteLine($"{nameof(IvxrPlugin)} initialization started.");

			m_server = new PluginServer(Log);
			m_server.Start();
		}

		public void Update()
		{
			//m_log.WriteLine("Update called.");
		}



		#region IDisposable Support
		private bool alreadyDisposed = false; // To detect redundant calls

		protected virtual void Dispose(bool disposing)
		{
			if (!alreadyDisposed)
			{
				if (disposing)
				{
					// dispose managed state (managed objects).
					m_server.Stop();
				}

				// TODO: free unmanaged resources (unmanaged objects) and override a finalizer below.
				// TODO: set large fields to null.

				alreadyDisposed = true;
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

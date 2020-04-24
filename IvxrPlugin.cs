using System;
using VRage.Game.ObjectBuilders.Gui;
using VRage.Plugins;
using VRage.Utils;

namespace EU.Iv4xr.SePlugin
{
	public class IvxrPlugin : IPlugin
	{
		public static MyLog Log { get; private set; }

		private PluginServer m_server;

		static IvxrPlugin()
		{
			Log = new MyLog(alwaysFlush: true);
			Log.Init("ivxr-plugin.log", new System.Text.StringBuilder("0.1.0"));
		}

		public void Init(object gameInstance)
		{
			Log.WriteLine($"{nameof(IvxrPlugin)} initialization started.");

			m_server = new PluginServer();
			m_server.Start();
		}

		public void Update()
		{
			//m_log.WriteLine("Update called.");
		}



		#region IDisposable Support
		private bool disposedValue = false; // To detect redundant calls

		protected virtual void Dispose(bool disposing)
		{
			if (!disposedValue)
			{
				if (disposing)
				{
					// dispose managed state (managed objects).
					m_server.Stop();
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

using Iv4xr.PluginLib;
using Iv4xr.SePlugin.SeLib;
using System;
using System.Collections.Generic;
using System.Text;
using Iv4xr.SePlugin.Control;

namespace Iv4xr.SePlugin
{
	public class IvxrPluginContext : IDisposable
	{
		public ILog Log { get; private set; }
		public readonly Dispatcher Dispatcher;

		private readonly RequestQueue m_requestQueue = new RequestQueue();

		private readonly PluginServer m_server;

		private readonly Observer m_observer = new Observer();

		public IvxrPluginContext()
		{
			var seLog = new SeLog(alwaysFlush: true);
			seLog.Init("ivxr-plugin.log");
			Log = seLog;

			m_server = new PluginServer(Log, m_requestQueue);
			Dispatcher = new Dispatcher(m_requestQueue, m_observer);
		}

		public void StartServer()
		{
			m_server.Start();
		}

		public void EndSession()
		{
			Log.WriteLine("Ending session.");

			m_observer.EndSession();
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

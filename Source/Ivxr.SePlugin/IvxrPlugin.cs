using Iv4xr.PluginLib;
using Iv4xr.SePlugin.SeLib;
using System;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Control;
using VRage.Game.ObjectBuilders.Gui;
using VRage.Plugins;
using VRage.Utils;
using Sandbox.Game.World;

namespace Iv4xr.SePlugin
{
	public class IvxrPlugin : IPlugin
	{
		public static IvxrPluginContext Context { get; private set; }

		// Shortcuts
		public static ILog Log { get; private set; }
		public static Dispatcher Dispatcher { get; private set; }

		public void Init(object gameInstance)
		{
			if (Context != null)
			{
				Log.WriteLine("Init already called.");
				return;
			}

			Context = new IvxrPluginContext();

			Dispatcher = Context.Dispatcher;

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
	}
}

using EU.Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using Iv4xr.SeGameLib.Json;

namespace Iv4xr.SeGameLib.Control
{
	public class Controller
	{
		private readonly RequestQueue m_requestQueue;

		private readonly IObserver m_observer;

		private readonly Jsoner m_jsoner = new Jsoner();

		public Controller(RequestQueue requestQueue, IObserver observer)
		{
			m_requestQueue = requestQueue;
			m_observer = observer;
		}

		public void ProcessRequests()
		{
			while (m_requestQueue.Requests.TryDequeue(out Request request))
			{
				// Assuming observation request for now.
				var jsonReply = m_jsoner.ToJson(m_observer.GetObservation());

				m_requestQueue.Replies.Add(
					new Request(request.ClientStream, message: jsonReply));
			}
		}
	}
}

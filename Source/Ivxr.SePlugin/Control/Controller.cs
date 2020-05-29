using EU.Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;

namespace EU.Iv4xr.SePlugin.Control
{
	public class Controller
	{
		private readonly RequestQueue m_requestQueue;

		public Controller(RequestQueue requestQueue)
		{
			m_requestQueue = requestQueue;
		}

		public void ProcessRequests()
		{
			while (m_requestQueue.Requests.TryDequeue(out Request request))
			{
				//log.WriteLine("dequeued: " + request.Message);

				m_requestQueue.Replys.Add(
					new Request(request.ClientStream, $"Got {request.Message.Length} bytes, thanks!"));
			}
		}
	}
}

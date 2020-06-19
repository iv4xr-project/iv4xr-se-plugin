using Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using Iv4xr.SePlugin.Json;

namespace Iv4xr.SePlugin.Control
{
	public class Dispatcher
	{
		private readonly RequestQueue m_requestQueue;

		private readonly IObserver m_observer;

		private readonly Jsoner m_jsoner = new Jsoner();

		public Dispatcher(RequestQueue requestQueue, IObserver observer)
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

				/*
				var character = m_observer.GetCharacter();

				var entityController = character.ControllerInfo.Controller;

				entityController.ControlledEntity.MoveAndRotate(new VRageMath.Vector3(0, 0, 0.1), Vector2.Zero, 0);
				*/
			}
		}
	}
}

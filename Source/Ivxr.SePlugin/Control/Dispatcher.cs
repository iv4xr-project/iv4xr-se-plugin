using Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using Iv4xr.SePlugin.Json;
using Iv4xr.SePlugin.WorldModel;
using Iv4xr.PluginLib.Comm;

namespace Iv4xr.SePlugin.Control
{
	public class Dispatcher
	{
		public ILog Log { get; set; }

		private readonly RequestQueue m_requestQueue;

		private readonly IObserver m_observer;

		private readonly ICharacterController m_controller;

		private readonly Jsoner m_jsoner = new Jsoner();

		public Dispatcher(RequestQueue requestQueue, IObserver observer, ICharacterController controller)
		{
			m_requestQueue = requestQueue;
			m_observer = observer;
			m_controller = controller;
		}

		public void ProcessRequests()
		{
			while (m_requestQueue.Requests.TryDequeue(out RequestItem request))
			{
				string jsonReply;

				try
				{
					jsonReply = ProcessSingleRequest(request);
				}
				catch (Exception ex)
				{
					Log.WriteLine($"Error processing a request: {ex.Message}");
					Log.WriteLine($"Full request: \"{request.Message}\"");
					jsonReply = "false";  // Simple error response, details can be learned from the log.
				}

				m_requestQueue.Replies.Add(
					new RequestItem(request.ClientStream, message: jsonReply));
			}
		}

		private string ProcessSingleRequest(RequestItem request)
		{
			// Skip prefix "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\""
			var commandName = request.Message.Substring(36, 10);

			Log?.WriteLine($"{nameof(Dispatcher)} command prefix: '{commandName}'.");

			if (commandName.StartsWith("DONOTHING"))
			{
				// Just observe.
			}
			else if (commandName.StartsWith("MOVETOWARD"))
			{
				var requestShell = m_jsoner.ToObject<SeRequestShell<AgentCommand<MoveCommandArgs>>>(request.Message);
				var moveCommandArgs = requestShell.Arg.Arg;

				Log?.WriteLine($"Move indicator: {moveCommandArgs.MoveIndicator}");
				m_controller.Move(moveCommandArgs.MoveIndicator);
			}
			else
			{
				// TODO(PP): Maybe just log error and continue.
				throw new NotImplementedException($"Uknown agent command: {commandName}");
			}

			return m_jsoner.ToJson(m_observer.GetObservation());
		}
	}
}

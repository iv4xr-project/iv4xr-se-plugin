using Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using Iv4xr.SePlugin.Json;
using Iv4xr.SePlugin.WorldModel;
using Iv4xr.PluginLib.Comm;
using VRageMath;

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
					Log.Exception(ex, "Error processing a request");
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

			if (commandName.StartsWith("OBSERVE") || commandName.StartsWith("DONOTHING"))  // DONOTHING is obsolete.
			{
				// Just observe.
			}
			else if (commandName.StartsWith("MOVE_ROTAT"))  // MOVE_ROTATE
			{
				var requestShell = m_jsoner.ToObject<SeRequestShell<AgentCommand<MoveAndRotateArgs>>>(request.Message);

				m_controller.Move(requestShell.Arg.Arg);
			}
			else if (commandName.StartsWith("MOVETOWARD"))  // TODO(PP): Remove.
			{
				var requestShell = m_jsoner.ToObject<SeRequestShell<AgentCommand<MoveCommandArgs>>>(request.Message);
				var moveCommandArgs = requestShell.Arg.Arg;

				Log?.WriteLine($"Move indicator: {moveCommandArgs.MoveIndicator}");
				m_controller.Move(moveCommandArgs.MoveIndicator, Vector2.Zero, 0.0f);
			}
			else if (commandName.StartsWith("INTERACT"))
			{ 
				var requestShell = m_jsoner.ToObject<SeRequestShell<AgentCommand<InteractionArgs>>>(request.Message);

				m_controller.Interact(requestShell.Arg.Arg);
			}
			else
			{
				throw new NotImplementedException($"Uknown agent command: {commandName}");
			}

			return m_jsoner.ToJson(m_observer.GetObservation());
		}
	}
}

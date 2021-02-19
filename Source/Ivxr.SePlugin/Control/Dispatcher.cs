using Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using Iv4xr.SePlugin.Json;

namespace Iv4xr.SePlugin.Control
{
    using CommandDict = Dictionary<String, IStringCommand>;

    public class Dispatcher
    {
        public ILog Log { get; set; }

        private readonly RequestQueue m_requestQueue;

        private readonly IObserver m_observer;

        private readonly ICharacterController m_controller;

        private readonly Jsoner m_jsoner = new Jsoner();

        private readonly DispatcherContext m_context;

        private readonly CommandDict m_commands;

        public Dispatcher(RequestQueue requestQueue, IObserver observer, ICharacterController controller, CommandDict commands = null)
        {
            m_requestQueue = requestQueue;
            m_observer = observer;
            m_controller = controller;
            m_context = new DispatcherContext(observer, controller);
            m_commands = commands;
            if (m_commands == null)
            {
                m_commands = DefaultCommands();
            }
        }

        public void AddCommand(IStringCommand command)
        {
            m_commands[command.Cmd] = command;
        }

        private CommandDict DefaultCommands()
        {
            var commandList = new List<IStringCommand>
            {
                new ObserveCommand(),
                new MoveAndRotateCommand(),
                new MoveTowardCommand(),
                new InteractCommand()
            };
            var result = new CommandDict();
            foreach (var command in commandList)
            {
                result[command.Cmd] = command;
            }
            return result;
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
            var commandName = request.Message.Substring(36, 20).Split(new string[] { "\"" }, StringSplitOptions.None)[0];
            Log?.WriteLine($"{nameof(Dispatcher)} command prefix: '{commandName}'.");

            if (m_commands.ContainsKey(commandName))
            {
                var command = m_commands[commandName];
                return command.Execute(request.Message, m_context, m_jsoner);
            }
            throw new NotImplementedException($"Unknown agent command: {commandName}");
        }
    }
}

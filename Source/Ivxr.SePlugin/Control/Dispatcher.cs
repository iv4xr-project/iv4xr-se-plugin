using Iv4xr.PluginLib;
using System;
using System.Collections.Generic;
using Iv4xr.SePlugin.Json;
using Iv4xr.SePlugin.WorldModel;
using Iv4xr.PluginLib.Comm;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{

    using CommandDict = Dictionary<String, StringCommand>;

    public class DispatcherContext
    {
        public readonly IObserver observer;
        public readonly ICharacterController characterController;

        public DispatcherContext(IObserver observer, ICharacterController characterController)
        {
            this.observer = observer;
            this.characterController = characterController;
        }
    }

    public interface StringCommand
    {
        string Cmd { get; }
        string Execute(string message, DispatcherContext m_context, Jsoner m_jsoner);
    }

    public abstract class DispatcherCommand<I, O> : StringCommand
        where I : class
        where O : class
    {
        public string Cmd { get; }

        public DispatcherCommand(string cmd)
        {
            this.Cmd = cmd;
        }

        public string Execute(string message, DispatcherContext m_context, Jsoner m_jsoner)

        {
            var inputData = m_jsoner.ToObject<I>(message);
            var outputData = Execute(m_context, inputData);
            return m_jsoner.ToJson(outputData);
        }

        public abstract O Execute(DispatcherContext context, I data);
    }

    public class ObserveCommand : DispatcherCommand<SeRequestShell<AgentCommand<ObservationArgs>>, SeObservation>
    {

        public ObserveCommand() : base("OBSERVE")
        {}

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<ObservationArgs>> data)
        {
            return context.observer.GetObservation(data.Arg.Arg);
        }
    }

    public class MoveAndRotateCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveAndRotateArgs>>, SeObservation>
    {
        public MoveAndRotateCommand() : base("MOVE_ROTATE")
        {}

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<MoveAndRotateArgs>> data)
        {
            context.characterController.Move(data.Arg.Arg);
            return context.observer.GetObservation();
        }
    }

    public class MoveTowardCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveCommandArgs>>, SeObservation>
    {
        public MoveTowardCommand() : base("MOVETOWARD")
        {}

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<MoveCommandArgs>> data)
        {
            context.characterController.Move(data.Arg.Arg.MoveIndicator, Vector2.Zero, 0.0f);
            return context.observer.GetObservation();

        }
    }

    public class InteractCommand : DispatcherCommand<SeRequestShell<AgentCommand<InteractionArgs>>, SeObservation>
    {
        public InteractCommand() : base("INTERACT")
        {}

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<InteractionArgs>> data)
        {
            context.characterController.Interact(data.Arg.Arg);
            return context.observer.GetObservation();
        }
    }


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

        public void AddCommand(StringCommand command)
        {
            m_commands[command.Cmd] = command;
        }

        private CommandDict DefaultCommands()
        {

            var commandList = new List<StringCommand>
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

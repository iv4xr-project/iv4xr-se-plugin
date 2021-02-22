using Iv4xr.SePlugin.Json;
using Iv4xr.SePlugin.WorldModel;
using Iv4xr.PluginLib.Comm;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class DispatcherContext
    {
        public readonly IObserver Observer;
        public readonly ICharacterController CharacterController;

        public DispatcherContext(IObserver observer, ICharacterController characterController)
        {
            this.Observer = observer;
            this.CharacterController = characterController;
        }
    }

    public interface IStringCommand
    {
        string Cmd { get; }
        string Execute(string message, DispatcherContext context, Jsoner jsoner);
    }

    public abstract class DispatcherCommand<TInput, TOutput> : IStringCommand
        where TInput : class
        where TOutput : class
    {
        public string Cmd { get; }

        protected DispatcherCommand(string cmd)
        {
            this.Cmd = cmd;
        }

        public string Execute(string message, DispatcherContext context, Jsoner jsoner)
        {
            var inputData = jsoner.ToObject<TInput>(message);
            var outputData = Execute(context, inputData);
            return jsoner.ToJson(outputData);
        }

        public abstract TOutput Execute(DispatcherContext context, TInput data);
    }

    public class ObserveCommand : DispatcherCommand<SeRequestShell<AgentCommand<ObservationArgs>>, SeObservation>
    {

        public ObserveCommand() : base("OBSERVE")
        { }

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<ObservationArgs>> data)
        {
            return context.Observer.GetObservation(data.Arg.Arg);
        }
    }

    public class MoveAndRotateCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveAndRotateArgs>>, SeObservation>
    {
        public MoveAndRotateCommand() : base("MOVE_ROTATE")
        { }

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<MoveAndRotateArgs>> data)
        {
            context.CharacterController.Move(data.Arg.Arg);
            return context.Observer.GetObservation();
        }
    }

    public class MoveTowardCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveCommandArgs>>, SeObservation>
    {
        public MoveTowardCommand() : base("MOVETOWARD")
        { }

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<MoveCommandArgs>> data)
        {
            context.CharacterController.Move(data.Arg.Arg.MoveIndicator, Vector2.Zero, 0.0f);
            return context.Observer.GetObservation();
        }
    }

    public class InteractCommand : DispatcherCommand<SeRequestShell<AgentCommand<InteractionArgs>>, SeObservation>
    {
        public InteractCommand() : base("INTERACT")
        { }

        public override SeObservation Execute(DispatcherContext context, SeRequestShell<AgentCommand<InteractionArgs>> data)
        {
            context.CharacterController.Interact(data.Arg.Arg);
            return context.Observer.GetObservation();
        }
    }
}

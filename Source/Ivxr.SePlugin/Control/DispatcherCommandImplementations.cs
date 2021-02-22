using Iv4xr.SePlugin.WorldModel;
using Iv4xr.PluginLib.Comm;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class ObserveCommand : DispatcherCommand<SeRequestShell<AgentCommand<ObservationArgs>>, SeObservation>
    {
        public ObserveCommand() : base("OBSERVE")
        {
        }

        protected override SeObservation Execute(DispatcherContext context,
            SeRequestShell<AgentCommand<ObservationArgs>> data)
        {
            return context.Observer.GetObservation(data.Arg.Arg);
        }
    }

    public class
        MoveAndRotateCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveAndRotateArgs>>, SeObservation>
    {
        public MoveAndRotateCommand() : base("MOVE_ROTATE")
        {
        }

        protected override SeObservation Execute(DispatcherContext context,
            SeRequestShell<AgentCommand<MoveAndRotateArgs>> data)
        {
            context.CharacterController.Move(data.Arg.Arg);
            return context.Observer.GetObservation();
        }
    }

    public class MoveTowardCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveCommandArgs>>, SeObservation>
    {
        public MoveTowardCommand() : base("MOVETOWARD")
        {
        }

        protected override SeObservation Execute(DispatcherContext context,
            SeRequestShell<AgentCommand<MoveCommandArgs>> data)
        {
            context.CharacterController.Move(data.Arg.Arg.MoveIndicator, Vector2.Zero, 0.0f);
            return context.Observer.GetObservation();
        }
    }

    public class InteractCommand : DispatcherCommand<SeRequestShell<AgentCommand<InteractionArgs>>, SeObservation>
    {
        public InteractCommand() : base("INTERACT")
        {
        }

        protected override SeObservation Execute(DispatcherContext context,
            SeRequestShell<AgentCommand<InteractionArgs>> data)
        {
            context.CharacterController.Interact(data.Arg.Arg);
            return context.Observer.GetObservation();
        }
    }
}
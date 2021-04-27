using Iv4xr.PluginLib.Comm;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class ObserveCommand : DispatcherCommand<SeRequestShell<AgentCommand<ObservationArgs>>, Observation>
    {
        public ObserveCommand() : base("OBSERVE")
        {
        }

        protected override Observation Execute(ISpaceEngineers se,
            SeRequestShell<AgentCommand<ObservationArgs>> data)
        {
            return se.Observer.GetObservation(data.Arg.Arg);
        }
    }

    public class
            MoveAndRotateCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveAndRotateArgs>>, Observation>
    {
        public MoveAndRotateCommand() : base("MOVE_ROTATE")
        {
        }

        protected override Observation Execute(ISpaceEngineers se,
            SeRequestShell<AgentCommand<MoveAndRotateArgs>> data)
        {
            se.Character.Move(data.Arg.Arg);
            return se.Observer.GetObservation();
        }
    }

    public class MoveTowardCommand : DispatcherCommand<SeRequestShell<AgentCommand<MoveCommandArgs>>, Observation>
    {
        public MoveTowardCommand() : base("MOVETOWARD")
        {
        }

        protected override Observation Execute(ISpaceEngineers se,
            SeRequestShell<AgentCommand<MoveCommandArgs>> data)
        {
            se.Character.Move(data.Arg.Arg.MoveIndicator, Vector2.Zero, 0.0f);
            return se.Observer.GetObservation();
        }
    }

    public class InteractCommand : DispatcherCommand<SeRequestShell<AgentCommand<InteractionArgs>>, Observation>
    {
        public InteractCommand() : base("INTERACT")
        {
        }

        protected override Observation Execute(ISpaceEngineers se,
            SeRequestShell<AgentCommand<InteractionArgs>> data)
        {
            se.Items.Interact(data.Arg.Arg);
            return se.Observer.GetObservation();
        }
    }
}

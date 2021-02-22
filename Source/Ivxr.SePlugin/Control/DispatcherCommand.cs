using Iv4xr.SePlugin.Json;

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

        protected abstract TOutput Execute(DispatcherContext context, TInput data);
    }
}

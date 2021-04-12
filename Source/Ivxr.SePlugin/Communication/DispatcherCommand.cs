using Iv4xr.SePlugin.Json;

namespace Iv4xr.SePlugin.Communication
{
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

        /// <summary>
        /// Deserialize the command, execute & serialize response.
        /// </summary>
        public string Execute(string message, DispatcherContext context, Jsoner jsoner)
        {
            var inputData = jsoner.ToObject<TInput>(message);
            var outputData = Execute(context, inputData);
            return jsoner.ToJson(outputData);
        }

        /// <summary>
        /// Execute the command while consuming and returning typed non-serialized data.
        /// </summary>
        protected abstract TOutput Execute(DispatcherContext context, TInput data);
    }
}
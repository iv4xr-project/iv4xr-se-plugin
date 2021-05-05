using System;
using System.Collections.Concurrent;
using System.Threading.Tasks;

namespace Iv4xr.SePlugin.Communication
{
    internal class FuncToRunOnGameLoop
    {
        private readonly Func<dynamic> m_function;
        private readonly BlockingCollection<dynamic> m_result;

        public FuncToRunOnGameLoop(Func<dynamic> function)
        {
            m_function = function;
            m_result = new BlockingCollection<dynamic>();
        }

        public void Execute()
        {
            m_result.Add(m_function());
        }

        public dynamic Take()
        {
            return m_result.Take();
        }
    }

    public class FuncActionDispatcher
    {
        private readonly ConcurrentQueue<FuncToRunOnGameLoop> m_functions =
                new ConcurrentQueue<FuncToRunOnGameLoop>();

        public async Task<dynamic> EnqueueAsync(Func<dynamic> func)
        {
            var functionToRun = new FuncToRunOnGameLoop(func);
            m_functions.Enqueue(functionToRun);
            await Task.Yield();
            return functionToRun.Take();
        }

        public async Task<object> EnqueueAsync(Action func)
        {
            return await EnqueueAsync(ActionToNullReturningFunc(func));
        }

        public dynamic Enqueue(Func<dynamic> func)
        {
            var taskToRun = new FuncToRunOnGameLoop(func);
            m_functions.Enqueue(taskToRun);
            Task.Yield();
            return taskToRun.Take();
        }

        public object Enqueue(Action func)
        {
            return Enqueue(ActionToNullReturningFunc(func));
        }

        public void CallEverything()
        {
            while (m_functions.TryDequeue(out var taskToRun))
            {
                taskToRun.Execute();
            }
        }

        private static Func<dynamic> ActionToNullReturningFunc(Action action)
        {
            return () =>
            {
                action();
                return null;
            };
        }
    }
}

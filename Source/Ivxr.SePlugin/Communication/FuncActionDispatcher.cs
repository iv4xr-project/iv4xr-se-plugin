using System;
using System.Collections.Concurrent;
using System.Reflection;
using System.Runtime.ExceptionServices;
using System.Threading.Tasks;
using Iv4xr.PluginLib;

namespace Iv4xr.SePlugin.Communication
{
    internal class CallResult
    {
        public dynamic ReturnValue { private set; get; }
        public Exception Exception { private set; get; }
        public bool IsException { private set; get; }

        public dynamic ReturnOrThrow()
        {
            if (IsException)
            {
                if (Exception is TargetInvocationException && Exception.InnerException != null)
                {
                    ExceptionDispatchInfo.Capture(Exception.InnerException).Throw();
                }
                else
                {
                    ExceptionDispatchInfo.Capture(Exception).Throw();
                }
            }

            return ReturnValue;
        }

        public static CallResult FromException(Exception exception)
        {
            return new CallResult()
            {
                Exception = exception,
                IsException = true,
            };
        }

        public static CallResult FromResult(dynamic result)
        {
            return new CallResult()
            {
                ReturnValue = result,
                IsException = false,
            };
        }
    }

    internal class FuncToRunOnGameLoop
    {
        private readonly Func<object> m_function;
        private readonly BlockingCollection<CallResult> m_result;
        private readonly ILog m_log;

        public FuncToRunOnGameLoop(Func<dynamic> function, ILog log)
        {
            m_function = function;
            m_result = new BlockingCollection<CallResult>();
            m_log = log;
        }

        public void Execute()
        {
            try
            {
                m_result.Add(CallResult.FromResult(m_function()));
            }
            catch (Exception exception)
            {
                m_log.Exception(exception);
                m_result.Add(CallResult.FromException(exception));
            }
        }

        public CallResult Take()
        {
            return m_result.Take();
        }
    }

    public interface ICallable
    {
        object Call(Func<object> func);
    }

    public class DirectCallDispatcher : ICallable
    {
        public object Call(Func<object> func)
        {
            return func();
        }
    }

    public class FuncActionDispatcher : ICallable
    {
        private readonly ILog m_log;

        public FuncActionDispatcher(ILog log)
        {
            m_log = log;
        }

        private readonly ConcurrentQueue<FuncToRunOnGameLoop> m_functions =
                new ConcurrentQueue<FuncToRunOnGameLoop>();


        public object Call(Func<object> func)
        {
            var taskToRun = new FuncToRunOnGameLoop(func, m_log);
            m_functions.Enqueue(taskToRun);
            Task.Yield();
            return taskToRun.Take().ReturnOrThrow();
        }

        public void Call(Action func)
        {
            Call(ActionToNullReturningFunc(func));
        }

        public void CallEverything()
        {
            while (m_functions.TryDequeue(out var taskToRun))
            {
                taskToRun.Execute();
            }
        }

        private static Func<object> ActionToNullReturningFunc(Action action)
        {
            return () =>
            {
                action();
                return null;
            };
        }
    }
}

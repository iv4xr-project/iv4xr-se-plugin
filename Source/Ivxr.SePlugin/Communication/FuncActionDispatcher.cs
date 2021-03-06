﻿using System;
using System.Collections.Concurrent;
using System.Threading.Tasks;

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
                throw Exception;
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
        private readonly Func<dynamic> m_function;
        private readonly BlockingCollection<CallResult> m_result;

        public FuncToRunOnGameLoop(Func<dynamic> function)
        {
            m_function = function;
            m_result = new BlockingCollection<CallResult>();
        }

        public void Execute()
        {
            try
            {
                m_result.Add(CallResult.FromResult(m_function()));
            }
            catch (Exception exception)
            {
                m_result.Add(CallResult.FromException(exception));
            }
        }

        public CallResult Take()
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
            return functionToRun.Take().ReturnOrThrow();
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
            return taskToRun.Take().ReturnOrThrow();
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

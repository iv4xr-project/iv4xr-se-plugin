using System;
using Iv4xr.PluginLib;

namespace Iv4xr.SePlugin.Communication
{
    public class MethodCallContext
    {
        public readonly FuncActionDispatcher BeforeSimulation;
        public readonly FuncActionDispatcher AfterSimulation;
        public readonly FuncActionDispatcher MainThread;
        public readonly ICallable DirectCall;
        public readonly CallTarget DefaultCallTarget;

        public MethodCallContext(ILog log) : this(
            new FuncActionDispatcher(log),
            new FuncActionDispatcher(log),
            new FuncActionDispatcher(log),
            new DirectCallDispatcher(),
            CallTarget.MainThread
        )
        {
        }

        public ICallable GetCallable(CallTarget callTarget)
        {
            switch (callTarget)
            {
                case CallTarget.CurrentThread:
                    return DirectCall;
                case CallTarget.BeforeSimulation:
                    return BeforeSimulation;
                case CallTarget.AfterSimulation:
                    return AfterSimulation;
                case CallTarget.MainThread:
                    return MainThread;

                default:
                    throw new ArgumentOutOfRangeException(nameof(callTarget), callTarget, null);
            }
        }

        public MethodCallContext(FuncActionDispatcher beforeSimulation,
            FuncActionDispatcher afterSimulation, FuncActionDispatcher mainThread, ICallable directCall,
            CallTarget callTarget)
        {
            BeforeSimulation = beforeSimulation;
            AfterSimulation = afterSimulation;
            MainThread = mainThread;
            DirectCall = directCall;
            DefaultCallTarget = callTarget;
        }
    }
}

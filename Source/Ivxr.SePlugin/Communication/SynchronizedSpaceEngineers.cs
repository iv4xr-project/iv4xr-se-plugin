using System;
using ImpromptuInterface;
using Iv4xr.SpaceEngineers;

namespace Iv4xr.SePlugin.Communication
{
    [AttributeUsage(AttributeTargets.Method, Inherited = false)]
    public class RunOutsideGameLoop : Attribute
    {
    }

    public class RunOnMainThread : Attribute
    {
    }

    public class SynchronizedSpaceEngineers : ISpaceEngineers
    {
        public ICharacterController Character { get; }
        public ISessionController Session { get; }
        public IItems Items { get; }
        public IObserver Observer { get; }
        public IDefinitions Definitions { get; }
        public IBlocks Blocks { get; }
        public ISpaceEngineersAdmin Admin { get; }
        public IScreens Screens { get; }


        public SynchronizedSpaceEngineers(ISpaceEngineers se, FuncActionDispatcher beforeSimulationFuncActionDispatcher,
            FuncActionDispatcher mainThreadFuncActionDispatcher)
        {
            Character = new GameLoopDynamicProxy<ICharacterController>(se.Character,
                        beforeSimulationFuncActionDispatcher, mainThreadFuncActionDispatcher)
                    .ActLike<ICharacterController>();
            Session = new GameLoopDynamicProxy<ISessionController>(se.Session, beforeSimulationFuncActionDispatcher,
                        mainThreadFuncActionDispatcher)
                    .ActLike<ISessionController>();
            Items = new GameLoopDynamicProxy<IItems>(se.Items, beforeSimulationFuncActionDispatcher,
                mainThreadFuncActionDispatcher).ActLike<IItems>();
            Observer = new GameLoopDynamicProxy<IObserver>(se.Observer, beforeSimulationFuncActionDispatcher,
                        mainThreadFuncActionDispatcher)
                    .ActLike<IObserver>();
            Definitions = new GameLoopDynamicProxy<IDefinitions>(se.Definitions, beforeSimulationFuncActionDispatcher,
                        mainThreadFuncActionDispatcher)
                    .ActLike<IDefinitions>();
            Blocks =
                    new GameLoopDynamicProxy<IBlocks>(se.Blocks, beforeSimulationFuncActionDispatcher,
                                mainThreadFuncActionDispatcher)
                            .ActLike<IBlocks>();
            Admin = new GameLoopDynamicProxy<ISpaceEngineersAdmin>(se.Admin, beforeSimulationFuncActionDispatcher,
                        mainThreadFuncActionDispatcher)
                    .ActLike<ISpaceEngineersAdmin>();
            Screens = new GameLoopDynamicProxy<IScreens>(se.Screens, beforeSimulationFuncActionDispatcher,
                        mainThreadFuncActionDispatcher)
                    .ActLike<IScreens>();
        }
    }
}

using System;
using ImpromptuInterface;
using Iv4xr.SpaceEngineers;

namespace Iv4xr.SePlugin.Communication
{
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


        public SynchronizedSpaceEngineers(ISpaceEngineers se, MethodCallContext methodCallContext)
        {
            Character = new GameLoopDynamicProxy<ICharacterController>(se.Character, methodCallContext)
                    .ActLike<ICharacterController>();
            Session = new GameLoopDynamicProxy<ISessionController>(se.Session, methodCallContext)
                    .ActLike<ISessionController>();
            Items = new GameLoopDynamicProxy<IItems>(se.Items, methodCallContext).ActLike<IItems>();
            Observer = new GameLoopDynamicProxy<IObserver>(se.Observer, methodCallContext).ActLike<IObserver>();
            Definitions = new GameLoopDynamicProxy<IDefinitions>(se.Definitions, methodCallContext)
                    .ActLike<IDefinitions>();
            Blocks = new GameLoopDynamicProxy<IBlocks>(se.Blocks, methodCallContext).ActLike<IBlocks>();
            Admin = new GameLoopDynamicProxy<ISpaceEngineersAdmin>(se.Admin, methodCallContext)
                    .ActLike<ISpaceEngineersAdmin>();
            Screens = new GameLoopDynamicProxy<IScreens>(se.Screens, methodCallContext).ActLike<IScreens>();
        }
    }
}

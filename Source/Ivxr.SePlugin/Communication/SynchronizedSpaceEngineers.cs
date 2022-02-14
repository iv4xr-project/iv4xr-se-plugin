using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Threading.Tasks;
using ImpromptuInterface;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SePlugin.Communication
{
    public class GameLoopDynamicProxy<TType> : DynamicObject
    {
        private readonly TType m_instance;

        private readonly FuncActionDispatcher m_funcActionDispatcher;

        private readonly string[] m_directCallExceptions;

        public GameLoopDynamicProxy(TType instance, FuncActionDispatcher funcActionDispatcher,
            string[] directCallExceptions = null)
        {
            m_instance = instance;
            m_funcActionDispatcher = funcActionDispatcher;
            m_directCallExceptions = directCallExceptions;
        }

        private bool NeedsDirectCall(string methodName)
        {
            return m_directCallExceptions != null && m_directCallExceptions.Contains(methodName);
        }

        public override bool TryInvokeMember(InvokeMemberBinder binder, object[] args, out object result)
        {
            var methodInfo = m_instance.GetType().GetMethod(binder.Name);
            methodInfo.ThrowIfNull($"methodInfo {binder.Name}");
            if (NeedsDirectCall(methodInfo.Name))
            {
                result = methodInfo.Invoke(m_instance, args);
                return true;
            }

            object r = null;
            m_funcActionDispatcher.Enqueue(() => { return r = methodInfo.Invoke(m_instance, args); });
            result = r;
            return true;
        }
    }

    public abstract class AbstractServiceOnGameLoop
    {
        private readonly FuncActionDispatcher m_funcActionDispatcher;

        protected AbstractServiceOnGameLoop(FuncActionDispatcher funcActionDispatcher)
        {
            m_funcActionDispatcher = funcActionDispatcher;
        }

        protected async Task<dynamic> EnqueueAsync(Func<dynamic> func)
        {
            return await m_funcActionDispatcher.EnqueueAsync(func);
        }

        protected async Task<object> EnqueueAsync(Action func)
        {
            return await m_funcActionDispatcher.EnqueueAsync(func);
        }

        protected T Enqueue<T>(Func<T> func) where T : class
        {
            return m_funcActionDispatcher.Enqueue(func);
        }

        protected void Enqueue(Action func)
        {
            m_funcActionDispatcher.Enqueue(func);
        }
    }

    public class ScreensOnGameLoop : AbstractServiceOnGameLoop, IScreens
    {
        private readonly IScreens m_screens;

        public ScreensOnGameLoop(IScreens screens, FuncActionDispatcher funcActionDispatcher) : base(
            funcActionDispatcher)
        {
            m_screens = screens;
        }

        public string FocusedScreen()
        {
            return m_screens.FocusedScreen();
        }

        public void WaitUntilTheGameLoaded()
        {
            m_screens.WaitUntilTheGameLoaded();
        }

        public IMedicals Medicals => m_screens.Medicals;
        public ITerminal Terminal => m_screens.Terminal;                
    }

    public class SynchronizedSpaceEngineersAdmin : AbstractServiceOnGameLoop, ISpaceEngineersAdmin
    {
        private ISpaceEngineersAdmin Admin { get; }
        
        public SynchronizedSpaceEngineersAdmin(ISpaceEngineersAdmin admin, FuncActionDispatcher funcActionDispatcher) :
                base(funcActionDispatcher)
        {
            Admin = admin;
        }

        public void SetFrameLimitEnabled(bool enabled)
        {
            Enqueue(() => { Admin.SetFrameLimitEnabled(enabled); });
        }

        public ICharacterAdmin Character => Admin.Character;

        public IBlocksAdmin Blocks => Admin.Blocks;

        public IObserverAdmin Observer => Admin.Observer;

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


        public SynchronizedSpaceEngineers(ISpaceEngineers se, FuncActionDispatcher funcActionDispatcher)
        {
            Character = new GameLoopDynamicProxy<ICharacterController>(se.Character, funcActionDispatcher)
                    .ActLike<ICharacterController>();
            Session = se.Session;
            Items = new GameLoopDynamicProxy<IItems>(se.Items, funcActionDispatcher).ActLike<IItems>();
            Observer = new GameLoopDynamicProxy<IObserver>(se.Observer, funcActionDispatcher).ActLike<IObserver>();
            Definitions = new GameLoopDynamicProxy<IDefinitions>(se.Definitions, funcActionDispatcher)
                    .ActLike<IDefinitions>();
            Blocks = new GameLoopDynamicProxy<IBlocks>(se.Blocks, funcActionDispatcher).ActLike<IBlocks>();
            Admin = new SynchronizedSpaceEngineersAdmin(new SpaceEngineersAdmin(
                new GameLoopDynamicProxy<ICharacterAdmin>(se.Admin.Character, funcActionDispatcher)
                        .ActLike<ICharacterAdmin>(),
                new GameLoopDynamicProxy<IBlocksAdmin>(se.Admin.Blocks, funcActionDispatcher)
                        .ActLike<IBlocksAdmin>(),
                new GameLoopDynamicProxy<IObserverAdmin>(se.Admin.Observer, funcActionDispatcher)
                        .ActLike<IObserverAdmin>()
            ), funcActionDispatcher);
            Screens = new ScreensOnGameLoop(se.Screens, funcActionDispatcher);
        }
    }
}

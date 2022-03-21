using System;
using System.Dynamic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using ImpromptuInterface;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox;

namespace Iv4xr.SePlugin.Communication
{
    [AttributeUsage(AttributeTargets.Method, Inherited = false)]
    public class RunOutsideGameLoop : Attribute
    {
    }

    public class RunOnMainThread : Attribute
    {
    }

    public class GameLoopDynamicProxy<TType> : DynamicObject
    {
        private readonly TType m_instance;

        private readonly FuncActionDispatcher m_funcActionDispatcher;
        private readonly FuncActionDispatcher m_mainThreadFuncActionDispatcher;

        public GameLoopDynamicProxy(TType instance, FuncActionDispatcher funcActionDispatcher,
            FuncActionDispatcher mainThreadFuncActionDispatcher)
        {
            m_instance = instance;
            m_funcActionDispatcher = funcActionDispatcher;
            m_mainThreadFuncActionDispatcher = mainThreadFuncActionDispatcher;
        }

        private bool NeedsDirectCall(MethodInfo method)
        {
            return method.GetCustomAttributes(typeof(RunOutsideGameLoop)).Any();
        }

        private bool NeedsMainThread(MethodInfo method)
        {
            return method.GetCustomAttributes(typeof(RunOnMainThread)).Any();
        }

        public override bool TryInvokeMember(InvokeMemberBinder binder, object[] args, out object result)
        {
            var methodInfo = m_instance.GetType().GetMethod(binder.Name, args.Select(x => x.GetType()).ToArray());
            methodInfo.ThrowIfNull($"methodInfo {binder.Name}");
            if (NeedsDirectCall(methodInfo))
            {
                result = methodInfo.Invoke(m_instance, args);
                return true;
            }

            if (NeedsMainThread(methodInfo))
            {
                object r1 = null;
                /*
                 * TODO: this needs proper parallel synchronization, if "CallEverything" gets called sooner than the task gets enqueued, callback is never called.
                 * We will change this, once we hear back from SE team about the most correct way of calling things.
                 */
                MySandboxGame.Static.Invoke(() => { m_mainThreadFuncActionDispatcher.CallEverything(); },
                    "IV4XR-callbacks", -1, 2);
                m_mainThreadFuncActionDispatcher.Enqueue(() => { return r1 = methodInfo.Invoke(m_instance, args); });
                result = r1;
                return true;
            }

            object r = null;
            m_funcActionDispatcher.Enqueue(() => { return r = methodInfo.Invoke(m_instance, args); });
            result = r;
            return true;
        }

        public override bool TryGetMember(GetMemberBinder binder, out object result)
        {
            var p = m_instance.GetInstanceProperty<object>(binder.Name);
            p.ThrowIfNull($"Member {binder.Name} is null!");
            result = new GameLoopDynamicProxy<object>(p, m_funcActionDispatcher, m_mainThreadFuncActionDispatcher)
                    .ActLike(binder.ReturnType);
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

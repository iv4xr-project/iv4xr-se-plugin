using System;
using System.Dynamic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using ImpromptuInterface;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SpaceEngineers;

namespace Iv4xr.SePlugin.Communication
{
    [AttributeUsage(AttributeTargets.Method, Inherited = false)]
    public class RunOutsideGameLoop : Attribute
    {
    }

    public class GameLoopDynamicProxy<TType> : DynamicObject
    {
        private readonly TType m_instance;

        private readonly FuncActionDispatcher m_funcActionDispatcher;

        public GameLoopDynamicProxy(TType instance, FuncActionDispatcher funcActionDispatcher)
        {
            m_instance = instance;
            m_funcActionDispatcher = funcActionDispatcher;
        }

        private bool NeedsDirectCall(MethodInfo method)
        {
            return method.GetCustomAttributes(typeof(RunOutsideGameLoop)).Any();
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

            object r = null;
            m_funcActionDispatcher.Enqueue(() => { return r = methodInfo.Invoke(m_instance, args); });
            result = r;
            return true;
        }

        public override bool TryGetMember(GetMemberBinder binder, out object result)
        {
            var p = m_instance.GetInstanceProperty<object>(binder.Name);
            p.ThrowIfNull($"Member {binder.Name} is null!");
            result = new GameLoopDynamicProxy<object>(p, m_funcActionDispatcher).ActLike(binder.ReturnType);
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


        public SynchronizedSpaceEngineers(ISpaceEngineers se, FuncActionDispatcher funcActionDispatcher)
        {
            Character = new GameLoopDynamicProxy<ICharacterController>(se.Character, funcActionDispatcher)
                    .ActLike<ICharacterController>();
            Session = new GameLoopDynamicProxy<ISessionController>(se.Session, funcActionDispatcher).ActLike<ISessionController>();
            Items = new GameLoopDynamicProxy<IItems>(se.Items, funcActionDispatcher).ActLike<IItems>();
            Observer = new GameLoopDynamicProxy<IObserver>(se.Observer, funcActionDispatcher).ActLike<IObserver>();
            Definitions = new GameLoopDynamicProxy<IDefinitions>(se.Definitions, funcActionDispatcher)
                    .ActLike<IDefinitions>();
            Blocks = new GameLoopDynamicProxy<IBlocks>(se.Blocks, funcActionDispatcher).ActLike<IBlocks>();
            Admin = new GameLoopDynamicProxy<ISpaceEngineersAdmin>(se.Admin, funcActionDispatcher).ActLike<ISpaceEngineersAdmin>();
            Screens = new GameLoopDynamicProxy<IScreens>(se.Screens, funcActionDispatcher).ActLike<IScreens>();
        }
    }
}

using System;
using System.Dynamic;
using System.Linq;
using System.Reflection;
using ImpromptuInterface;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox;

namespace Iv4xr.SePlugin.Communication
{
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

        private void CheckRoles(MethodInfo methodInfo)
        {
            var attributes = methodInfo.GetCustomAttributes(typeof(RoleAttribute)).ToList();
            if (attributes.Count > 0)
            {
                var attribute = attributes.ToList().First() as RoleAttribute;
                var role = attribute.Value;
                CheckRole(role, DebugInfoCreator.Create());
            }
        }

        private void CheckRole(Role annotatedRole, DebugInfo debugInfo)
        {
            if (!debugInfo.CanDoRole(annotatedRole))
            {
                throw new InvalidOperationException($"Invalid role: {annotatedRole}");
            }
        }

        private void CheckInterfaceRoles(InvokeMemberBinder binder, object[] args)
        {
            m_instance.GetType().GetInterfaces().Select(
                interfaceType => interfaceType.GetMethod(binder.Name, args.Select(x => x.GetType()).ToArray())
            ).Where(t => t != null).ForEach(CheckRoles);
        }


        public override bool TryInvokeMember(InvokeMemberBinder binder, object[] args, out object result)
        {
            var methodInfo = m_instance.GetType().GetMethod(binder.Name, args.Select(x => x.GetType()).ToArray());
            methodInfo.ThrowIfNull($"methodInfo {binder.Name}");
            CheckRoles(methodInfo);
            CheckInterfaceRoles(binder, args);

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
}

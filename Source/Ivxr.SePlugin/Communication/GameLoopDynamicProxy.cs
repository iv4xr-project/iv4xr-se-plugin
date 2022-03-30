using System;
using System.Dynamic;
using System.Linq;
using System.Reflection;
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
        private readonly MethodCallContext m_methodCallContext;

        public GameLoopDynamicProxy(TType instance, MethodCallContext methodCallContext)
        {
            m_instance = instance;
            m_methodCallContext = methodCallContext;
        }

        private void CheckRoles(MethodInfo methodInfo)
        {
            var attributes = methodInfo.GetCustomAttributes(typeof(RoleAttribute)).ToList();
            if (attributes.Count > 0)
            {
                var attribute = attributes.ToList().First() as RoleAttribute;
                var role = attribute.Value;
                CheckRole(methodInfo, role, DebugInfoCreator.Create());
            }
        }

        private CallTarget GetCallTarget(MethodInfo methodInfo)
        {
            var callOn = methodInfo.GetCustomAttributes(typeof(CallOn)).ToList();
            return callOn.Count == 0 ? m_methodCallContext.DefaultCallTarget : ((CallOn)callOn[0]).Target;
        }

        private void CheckRole(MethodInfo methodInfo, Role annotatedRole, DebugInfo debugInfo)
        {
            if (!debugInfo.CanDoRole(annotatedRole))
            {
                throw new InvalidOperationException($"Invalid role: {annotatedRole} for {methodInfo}");
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

            var target = GetCallTarget(methodInfo);
            result = m_methodCallContext.GetCallable(target).Call(() => methodInfo.Invoke(m_instance, args));

            return true;
        }

        public override bool TryGetMember(GetMemberBinder binder, out object result)
        {
            var instanceProperty = m_instance.GetInstanceProperty<object>(binder.Name);
            instanceProperty.ThrowIfNull($"Member {binder.Name} is null!");
            result = new GameLoopDynamicProxy<object>(instanceProperty, m_methodCallContext).ActLike(binder.ReturnType);
            return true;
        }
    }
}

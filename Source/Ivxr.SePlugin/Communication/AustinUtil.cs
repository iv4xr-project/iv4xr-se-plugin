using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Reflection;
using AustinHarris.JsonRpc;

namespace Iv4xr.SePlugin.Communication
{
    public static class AustinUtil
    {
        
        //Based on Austin ServiceBinder.BindService
        public static void BindService<TType>(string sessionID, TType instance, string methodPrefix)
        {
            var smd = Handler.GetSessionHandler(sessionID).MetaData;
            foreach (MethodInfo method1 in ((IEnumerable<MethodInfo>)typeof(TType)
                        .GetMethods(BindingFlags.Instance | BindingFlags.Public | BindingFlags.DeclaredOnly))
            )
            {
                if (method1.Name.StartsWith("get_"))
                {
                    continue;
                }
                Dictionary<string, Type> parameters1 = new Dictionary<string, Type>();
                Dictionary<string, object> defaultValues = new Dictionary<string, object>();
                ParameterInfo[] parameters2 = method1.GetParameters();
                List<Type> typeList = new List<Type>();
                for (int index = 0; index < parameters2.Length; ++index)
                {
                    object[] customAttributes =
                            parameters2[index].GetCustomAttributes(typeof(JsonRpcParamAttribute), false);
                    string key;
                    if (customAttributes.Length != 0)
                    {
                        key = ((JsonRpcParamAttribute)customAttributes[0]).JsonParamName;
                        if (string.IsNullOrEmpty(key))
                            key = parameters2[index].Name;
                    }
                    else
                        key = parameters2[index].Name;

                    parameters1.Add(key, parameters2[index].ParameterType);
                    if (parameters2[index].IsOptional)
                        defaultValues.Add(key, parameters2[index].DefaultValue);
                }

                Type returnType = method1.ReturnType;
                parameters1.Add("returns", returnType);

                string methodName = methodPrefix + method1.Name;

                Delegate dele =
                        Delegate.CreateDelegate(Expression.GetDelegateType(parameters1.Values.ToArray<Type>()),
                            instance, method1);
                AddService(methodName, parameters1, defaultValues, dele, smd);
            }
        }

        private static void AddService(
            string method,
            Dictionary<string, Type> parameters,
            Dictionary<string, object> defaultValues,
            Delegate dele, SMD smd)
        {
            SMDService smdService = new SMDService(smd.transport, "JSON-RPC-2.0", parameters, defaultValues, dele);
            smd.Services.Add(method, smdService);
        }
    }
}

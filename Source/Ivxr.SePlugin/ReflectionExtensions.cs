using System;
using System.Reflection;

namespace Iv4xr.SePlugin
{
    public static class ReflectionExtensions
    {

        public static object CallMethod(this object instance, string methodName, object[] args)
        {
            instance.ThrowNREIfNull("Instance to call method on is null.");
            var method = instance.GetType().GetMethod(methodName, BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public);
            method.ThrowNREIfNull($"Method {methodName} is not found on object {instance.GetType().Name}");
            return method.Invoke(instance, args);
        }
        
        public static void SetInstanceField(this object instance, string fieldName, object value)
        {
            BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                                     | BindingFlags.Static;
            var t = instance.GetType();
            FieldInfo field = t.GetField(fieldName, bindFlags);
            field.SetValue(instance, value);
        }

        public static void SetInstanceProperty(this object instance, string fieldName, object value)
        {
            BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                                     | BindingFlags.Static;
            var t = instance.GetType();
            PropertyInfo field = t.GetProperty(fieldName, bindFlags);
            field.SetValue(instance, value);
        }
        
        
        public static T GetInstanceField<T>(this object instance, string fieldName)
        {
            BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                                     | BindingFlags.Static;
            var t = instance.GetType();
            var field = t.GetField(fieldName, bindFlags);
            field.ThrowNREIfNull($"Field {fieldName} not found for type {t.Name}");
            return (T)field.GetValue(instance);
        }

        public static T GetInstanceFieldOrThrow<T>(this object instance, string fieldName)
        {
            instance.ThrowNREIfNull($"Instance of type {instance.GetType()} to get the field from is null.");
            var field = instance.GetInstanceField<T>(fieldName);
            field.ThrowNREIfNull($"Field {fieldName} of type {typeof(T)} is null!");
            return field;

        }
    }
}

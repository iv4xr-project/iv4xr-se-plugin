using System.Reflection;

namespace Iv4xr.PluginLib
{
    public static class ReflectionExtensions
    {
        public static TReturnType CallMethod<TReturnType>(this object instance, string methodName, object[] args)
        {
            instance.ThrowIfNull("Instance to call method on is null.");
            var method = instance.GetType().GetMethod(methodName,
                BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public);
            method.ThrowIfNull($"Method {methodName} is not found on object {instance.GetType().Name}");
            return (TReturnType)method.Invoke(instance, args);
        }

        public static TReturnType CallMethod<TReturnType>(this object instance, string methodName)
        {
            instance.ThrowIfNull("Instance to call method on is null.");
            var method = instance.GetType().GetMethod(methodName,
                BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public);
            method.ThrowIfNull($"Method {methodName} is not found on object {instance.GetType().Name}");
            return (TReturnType)method.Invoke(instance, new object[] { });
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

        public static T GetInstanceProperty<T>(this object instance, string fieldName)
        {
            BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                                     | BindingFlags.Static;
            var t = instance.GetType();
            var field = t.GetProperty(fieldName, bindFlags);
            field.ThrowIfNull($"Field {fieldName} not found for type {t.Name}");
            return (T)field.GetValue(instance);
        }


        public static T GetInstanceField<T>(this object instance, string fieldName)
        {
            BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                                     | BindingFlags.Static;
            var t = instance.GetType();
            var field = t.GetField(fieldName, bindFlags);
            field.ThrowIfNull($"Field {fieldName} not found for type {t.Name}");
            return (T)field.GetValue(instance);
        }

        public static T GetInstanceFieldOrThrow<T>(this object instance, string fieldName)
        {
            instance.ThrowIfNull($"Instance of type {instance.GetType()} to get the field from is null.");
            var field = instance.GetInstanceField<T>(fieldName);
            field.ThrowIfNull($"Field {fieldName} of type {typeof(T)} is null!");
            return field;
        }
    }
}

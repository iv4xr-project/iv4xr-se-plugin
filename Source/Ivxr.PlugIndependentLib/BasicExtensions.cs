using System;

namespace Iv4xr.PluginLib
{
    public static class BasicExtensions
    {
        public static T ThrowIfNull<T>(this T instance, string paramName = null, string message = null)
        {
            if (instance == null)
            {
                throw new ArgumentNullException(paramName, message);
            }

            return instance;
        }

        public static int? ToNullIfMinusOne(this int value)
        {
            if (value == -1)
            {
                return null;
            }

            return value;
        }
    }
}

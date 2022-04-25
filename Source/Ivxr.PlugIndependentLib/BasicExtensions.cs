using System;

namespace Iv4xr.PluginLib
{
    public static class BasicExtensions
    {
        public static T ThrowIfNull<T>(this T instance, string message)
        {
            if (instance == null)
            {
                throw new ArgumentNullException();
            }

            return instance;
        }
    }
}

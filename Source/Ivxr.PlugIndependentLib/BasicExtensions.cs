using System;

namespace Iv4xr.PluginLib
{
    public static class BasicExtensions
    {
        public static void ThrowIfNull(this object instance, string message)
        {
            if (instance == null)
            {
                throw new ArgumentNullException(message);
            }
        }
    }
}

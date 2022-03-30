using System;

namespace Iv4xr.SePlugin.Communication
{
    public enum CallTarget
    {
        CurrentThread,
        MainThread,
        BeforeSimulation,
        AfterSimulation,
    }

    [AttributeUsage(AttributeTargets.Method)]
    public class CallOn : Attribute
    {
        public readonly CallTarget Target;

        public CallOn(CallTarget value)
        {
            Target = value;
        }
    }
}

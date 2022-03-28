using System;

namespace Iv4xr.SpaceEngineers
{
    [Flags]
    public enum Role
    {
        Admin = 1, // = server
        Observer = 2, // = anyone
        Game = 4, // = game instance server/client
    }

    [AttributeUsage(AttributeTargets.Method | AttributeTargets.Interface)]
    public class RoleAttribute : Attribute
    {
        public readonly Role Value;

        public RoleAttribute(Role value)
        {
            Value = value;
        }
    }

    public enum Purpose
    {
        Regular,
        Simplification,
        Hack,
    }

    [AttributeUsage(AttributeTargets.Method | AttributeTargets.Interface)]
    public class PurposeAttribute : Attribute
    {
        public readonly Purpose Purpose;

        public PurposeAttribute(Purpose purpose)
        {
            Purpose = purpose;
        }
    }
}

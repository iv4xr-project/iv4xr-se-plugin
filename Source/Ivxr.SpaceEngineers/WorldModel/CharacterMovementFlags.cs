using System;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    [Flags]
    public enum CharacterMovementFlags : byte
    {
        Jump = 1,
        Sprint = 2,
        FlyUp = 4,
        FlyDown = 8,
        Crouch = 16,
        Walk = 32
    }
}

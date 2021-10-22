namespace Iv4xr.PluginLib.WorldModel
{
    public enum CharacterMovementEnum: ushort
    {
                Standing   = CharacterMovement.Standing,
                Sitting    = CharacterMovement.Sitting,
                Crouching  = CharacterMovement.Crouching,
                Flying     = CharacterMovement.Flying,
                Falling    = CharacterMovement.Falling,
                Jump       = CharacterMovement.Jump,
                Died       = CharacterMovement.Died,
                Ladder     = CharacterMovement.Ladder,
                LadderOut  = CharacterMovement.LadderOut,
        
                RotatingLeft = CharacterMovement.RotatingLeft,
                RotatingRight = CharacterMovement.RotatingRight,
        
                Walking           = CharacterMovement.Forward,
                BackWalking       = CharacterMovement.Backward,
                WalkStrafingLeft  = CharacterMovement.Left,
                WalkStrafingRight = CharacterMovement.Right,
                WalkingRightFront = CharacterMovement.Right | CharacterMovement.Forward,
                WalkingRightBack  = CharacterMovement.Right | CharacterMovement.Backward,
                WalkingLeftFront  = CharacterMovement.Left | CharacterMovement.Forward,
                WalkingLeftBack   = CharacterMovement.Left | CharacterMovement.Backward,
        
                Running           = CharacterMovement.Forward | CharacterMovement.Fast,
                Backrunning       = CharacterMovement.Backward | CharacterMovement.Fast,
                RunStrafingLeft   = CharacterMovement.Left | CharacterMovement.Fast,
                RunStrafingRight  = CharacterMovement.Right | CharacterMovement.Fast,
                RunningRightFront = CharacterMovement.Right | CharacterMovement.Forward | CharacterMovement.Fast,
                RunningRightBack  = CharacterMovement.Right | CharacterMovement.Backward | CharacterMovement.Fast,
                RunningLeftFront  = CharacterMovement.Left | CharacterMovement.Forward | CharacterMovement.Fast,
                RunningLeftBack   = CharacterMovement.Left | CharacterMovement.Backward | CharacterMovement.Fast,
        
                CrouchWalking           = CharacterMovement.Forward | CharacterMovement.Crouching,
                CrouchBackWalking       = CharacterMovement.Backward | CharacterMovement.Crouching,
                CrouchStrafingLeft      = CharacterMovement.Left | CharacterMovement.Crouching,
                CrouchStrafingRight     = CharacterMovement.Right | CharacterMovement.Crouching,
                CrouchWalkingRightFront = CharacterMovement.Right | CharacterMovement.Forward | CharacterMovement.Crouching,
                CrouchWalkingRightBack  = CharacterMovement.Right | CharacterMovement.Backward | CharacterMovement.Crouching,
                CrouchWalkingLeftFront  = CharacterMovement.Left | CharacterMovement.Forward | CharacterMovement.Crouching,
                CrouchWalkingLeftBack   = CharacterMovement.Left | CharacterMovement.Backward | CharacterMovement.Crouching,
                CrouchRotatingLeft      = CharacterMovement.RotatingLeft | CharacterMovement.Crouching,
                CrouchRotatingRight     = CharacterMovement.RotatingRight | CharacterMovement.Crouching,
        
                Sprinting = CharacterMovement.Forward | CharacterMovement.VeryFast,
        
                LadderUp   = CharacterMovement.Ladder | CharacterMovement.Up,
                LadderDown = CharacterMovement.Ladder | CharacterMovement.Down,
                
                
                
    }

    public static class CharacterMovement
    {
        public const ushort MovementTypeMask      = 0x000f; // 4 bits (0 - 3) for movement type should be enough even for the future
        public const ushort MovementDirectionMask = 0x03f0; // 6 bits (4 - 9)
        public const ushort MovementSpeedMask     = 0x0c00; // 2 bits (10 - 11)
        public const ushort RotationMask          = 0x3000; // 2 bits (12 - 13)

        // The movement types are mutually exclusive - i.e. you cannot be sitting and crouching at the same time
        public const ushort Standing = 0;
        public const ushort Sitting = 1;
        public const ushort Crouching = 2;
        public const ushort Flying = 3;
        public const ushort Falling = 4;
        public const ushort Jump = 5;
        public const ushort Died = 6;
        public const ushort Ladder = 7;

        // Movement direction
        public const ushort NoDirection = 0;
        public const ushort Forward = 1 << 4;
        public const ushort Backward = 1 << 5;
        public const ushort Left = 1 << 6;
        public const ushort Right = 1 << 7;
        public const ushort Up = 1 << 8;
        public const ushort Down = 1 << 9;

        // Movement speed
        public const ushort NormalSpeed = 0;
        public const ushort Fast = 1 << 10;
        public const ushort VeryFast = 1 << 11;

        // Rotation
        public const ushort NotRotating = 0;
        public const ushort RotatingLeft = 1 << 12;
        public const ushort RotatingRight = 1 << 13;

        //Ladder
        public const ushort LadderOut = 1 << 14;
        
        public static ushort GetMode(this CharacterMovementEnum value)
        {
            return (ushort)((ushort)value & MovementTypeMask);
        }

        public static ushort GetDirection(this CharacterMovementEnum value)
        {
            return (ushort)((ushort)value & MovementDirectionMask);
        }

        public static ushort GetSpeed(this CharacterMovementEnum value)
        {
            return (ushort)((ushort)value & MovementSpeedMask); 
        }
    }
}

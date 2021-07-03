namespace Iv4xr.SePlugin.WorldModel
{
    public class MountPoint
    {
        public PlainVec3I Normal;
        public PlainVec3F Start;
        public PlainVec3F End;
        public bool Enabled;
        public bool Default;
        public bool PressurizedWhenOpen;
        public byte ExclusionMask;
        public byte PropertiesMask;
    }
}

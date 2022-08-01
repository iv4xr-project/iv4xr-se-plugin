using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class ParticleEffect
    {
        public string Name;
        public PlainVec3D Position;
    }

    public class Particles
    {
        public bool Enabled;
        public bool Paused;
        public List<ParticleEffect> Effects;
    }
}

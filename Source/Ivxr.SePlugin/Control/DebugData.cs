using System;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using VRage.Audio;
using VRage.Game;

namespace Iv4xr.SePlugin.Control
{
    public class DebugData : IDebug
    {
        public SoundBanks Sounds()
        {
            if (!(MyAudio.Static is MyXAudio2 audio))
                throw new InvalidOperationException("Cannot get audio info for this implementation");
            var cueBank = audio.GetInstanceFieldOrThrow<MyCueBank>("m_cueBank");
            return cueBank.ToSoundBanks();
        }

        public Particles Particles()
        {
            return new Particles()
            {
                Enabled = MyParticlesManager.Enabled,
                Paused = MyParticlesManager.Paused,
                Effects = MyParticlesManager.Effects.Select(item => item.ToParticleEffect()).ToList(),
            };
        }
    }
}

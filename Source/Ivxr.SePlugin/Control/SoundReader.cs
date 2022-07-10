using System;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using VRage.Audio;

namespace Iv4xr.SePlugin.Control
{
    public class SoundReader : ISound
    {
        public SoundBanks PlayingSounds()
        {
            if (!(MyAudio.Static is MyXAudio2 audio))
                throw new InvalidOperationException("Cannot get audio info for this implementation");
            var cueBank = audio.GetInstanceFieldOrThrow<MyCueBank>("m_cueBank");
            return cueBank.ToSoundBanks();

        }
    }
}

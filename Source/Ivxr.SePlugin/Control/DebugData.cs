using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.World;
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
            var cueBank = audio.GetInstanceField<MyCueBank>("m_cueBank");
            return cueBank.ThrowIfNull("m_cueBank", "The game sound is not initialized").ToSoundBanks();
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

        public CharacterAnimations CharacterAnimations()
        {
            var ctrl = MySession.Static.LocalCharacter.AnimationController.Controller;
            var layerNames = ctrl.GetInstanceFieldOrThrow<Dictionary<string, int>>("m_tableLayerNameToIndex");

            return new CharacterAnimations()
            {
                AnimationsPerLayer = layerNames.ToDictionary(layer => layer.Key,
                    layer => ctrl.GetLayerByName(layer.Key).CurrentNode.Name),
            };
        }
    }
}

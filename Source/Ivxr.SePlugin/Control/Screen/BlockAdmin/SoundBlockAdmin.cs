using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class SoundBlockAdmin : AbstractBlockAdmin<MySoundBlock>, ISoundBlockAdmin
    {
        public SoundBlockAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetRange(string blockId, float range)
        {
            BlockById(blockId).Range = range;
        }

        public void SetVolume(string blockId, float volume)
        {
            BlockById(blockId).Volume = volume;
        }

        public void SetLoopPeriod(string blockId, float loopPeriod)
        {
            BlockById(blockId).LoopPeriod = loopPeriod;
        }

        public void PlaySound(string blockId)
        {
            BlockById(blockId).RequestPlaySound();
        }

        public void StopSound(string blockId)
        {
            BlockById(blockId).RequestStopSound();
        }

        public void SelectSound(string blockId, string cueId)
        {
            BlockById(blockId).SelectSound(cueId, false);
        }
    }
}

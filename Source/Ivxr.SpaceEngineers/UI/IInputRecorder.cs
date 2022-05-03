using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.UI
{
    
    public interface IInput
    {
        void StartPlaying(List<FrameSnapshot> snapshots);
        void StopPlaying();
        void StartRecording();
        List<FrameSnapshot> StopRecording();

    }
}

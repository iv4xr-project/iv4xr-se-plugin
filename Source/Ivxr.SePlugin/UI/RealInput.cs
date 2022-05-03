using System.Collections.Generic;
using Iv4xr.SpaceEngineers.UI;

namespace Iv4xr.SePlugin.UI
{
    public class RealInput: IInput
    {
        public static RealInput Instance = new RealInput();
        private static FrameSnapshotController m_controller = new FrameSnapshotController();
        private FrameSnapshotRecorder m_recorder = new FrameSnapshotRecorder(new FrameSnapshotController());
        private FrameSnapshotPlayer m_player = new FrameSnapshotPlayer(new FrameSnapshotController());
        public void StartRecording()
        {
            m_recorder.StartRecording();
        }

        public List<FrameSnapshot> StopRecording()
        {
            return m_recorder.StopRecording();
        }

        public void StartPlaying(List<FrameSnapshot> snapshots)
        {
            m_player.StartPlaying(snapshots);
        }

        public void StopPlaying()
        {
            m_player.StopPlaying();
        }

        public void Tick()
        {
            m_player.Tick();
            m_recorder.Tick();
        }
    }
}

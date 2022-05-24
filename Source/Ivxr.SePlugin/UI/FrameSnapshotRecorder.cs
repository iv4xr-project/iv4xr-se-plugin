using System;
using System.Collections.Generic;
using Iv4xr.SpaceEngineers.UI;

namespace Iv4xr.SePlugin.UI
{
    public class FrameSnapshotRecorder
    {
        private readonly FrameSnapshotController m_controller;
        private readonly List<FrameSnapshot> m_snapshots = new List<FrameSnapshot>();
        private bool m_isRecording;

        public FrameSnapshotRecorder(FrameSnapshotController controller)
        {
            m_controller = controller;
        }

        public void StartRecording()
        {
            m_isRecording = true;
            m_snapshots.Clear();
        }

        public void Tick()
        {
            if (m_isRecording == true)
            {
                Record();
            }
        }

        private void Record()
        {
            m_snapshots.Add(m_controller.GetCurrent());
        }

        public List<FrameSnapshot> StopRecording()
        {
            if (!m_isRecording)
            {
                throw new InvalidOperationException("Cannot stop, not recording!");
            }

            m_isRecording = false;
            return m_snapshots;
        }

        public void Reset()
        {
            m_isRecording = false;
            m_snapshots.Clear();
        }
    }
}

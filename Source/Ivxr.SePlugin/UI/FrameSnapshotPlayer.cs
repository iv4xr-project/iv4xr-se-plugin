using System.Collections.Generic;
using System.Linq;
using Iv4xr.SpaceEngineers.UI;

namespace Iv4xr.SePlugin.UI
{
    public class FrameSnapshotPlayer
    {
        private List<FrameSnapshot> m_snapshots;
        private readonly FrameSnapshotController m_controller;
        private bool m_isPlaying;

        public FrameSnapshotPlayer(FrameSnapshotController controller)
        {
            m_controller = controller;
        }

        public void StartPlaying(List<FrameSnapshot> snapshots)
        {
            m_snapshots = snapshots;
            m_isPlaying = true;
        }

        public void StopPlaying()
        {
            m_isPlaying = false;
            m_snapshots = null;
        }

        public void Tick()
        {
            if (!m_isPlaying || m_snapshots == null || m_snapshots.Count == 0)
            {
                return;
            }
            
            m_controller.SetCurrent(m_snapshots.First());
            m_snapshots.RemoveAt(0);
        }
    }
}

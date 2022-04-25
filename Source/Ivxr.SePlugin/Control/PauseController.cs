using Iv4xr.SpaceEngineers;
using Sandbox;

namespace Iv4xr.SePlugin.Control
{
    public class PauseController: IPause
    {
        public void SetPaused(bool paused)
        {
            MySandboxGame.IsPaused = paused;
        }

        public bool IsPaused()
        {
            return MySandboxGame.IsPaused;
        }
    }
}

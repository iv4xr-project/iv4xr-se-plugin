using Iv4xr.SePlugin.Communication;
using Iv4xr.SpaceEngineers;

namespace Iv4xr.SePlugin.Control
{
    public class TestAdmin : ITestAdmin
    {
        [RunOutsideGameLoop]
        public void AdminOnly()
        {
        }

        [RunOutsideGameLoop]
        public void GameOnly()
        {
        }

        [RunOutsideGameLoop]
        public void ObserverOnly()
        {
        }
    }
}

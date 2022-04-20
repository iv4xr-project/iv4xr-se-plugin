using Iv4xr.SePlugin.Communication;
using Iv4xr.SpaceEngineers;
using static Iv4xr.SePlugin.Communication.CallTarget;

namespace Iv4xr.SePlugin.Control
{
    public class TestAdmin : ITestAdmin
    {
        [CallOn(CurrentThread)]
        public void AdminOnly()
        {
        }

        [CallOn(CurrentThread)]
        public void GameOnly()
        {
        }

        [CallOn(CurrentThread)]
        public void ObserverOnly()
        {
        }
    }
}

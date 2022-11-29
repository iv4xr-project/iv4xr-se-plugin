using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Cube;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class FunctionalBlockAdmin : AbstractBlockAdmin<MyFunctionalBlock>, IFunctionalBlockAdmin
    {
        public FunctionalBlockAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetEnabled(string blockId, bool enabled)
        {
            BlockById(blockId).Enabled = enabled;
        }
    }
}

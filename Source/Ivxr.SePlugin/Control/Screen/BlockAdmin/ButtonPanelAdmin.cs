using Iv4xr.SpaceEngineers;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class ButtonPanelAdmin : AbstractBlockAdmin<MyButtonPanel>, IButtonPanelAdmin
    {
        public ButtonPanelAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetAnyoneCanUse(string blockId, bool anyoneCanUse)
        {
            BlockById(blockId).AnyoneCanUse = anyoneCanUse;
        }
    }
}

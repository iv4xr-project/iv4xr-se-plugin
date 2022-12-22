using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class DoorBaseAdmin : AbstractBlockAdmin<MyDoorBase>, IDoorBaseAdmin
    {
        public DoorBaseAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetAnyoneCanUse(string blockId, bool anyoneCanUse)
        {
            BlockById(blockId).AnyoneCanUse = anyoneCanUse;
        }

        public void SetOpen(string blockId, bool open)
        {
            BlockById(blockId).Open = open;
        }
    }
}

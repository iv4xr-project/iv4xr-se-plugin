using System.Text;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Cube;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class TerminalBlockAdmin : AbstractBlockAdmin<MyTerminalBlock>, ITerminalBlockAdmin
    {
        public TerminalBlockAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetCustomName(string blockId, string customName)
        {
            BlockById(blockId).DisplayNameText = customName;
        }

        public void SetCustomData(string blockId, string customData)
        {
            BlockById(blockId).CustomData = customData;
        }
        
        public void SetShowInInventory(string blockId, bool showInInventory)
        {
            BlockById(blockId).ShowInInventory = showInInventory;
        }
        
        public void SetShowInTerminal(string blockId, bool showInTerminal)
        {
            BlockById(blockId).ShowInTerminal = showInTerminal;
        }
        
        public void SetShowOnHUD(string blockId, bool showOnHUD)
        {
            BlockById(blockId).ShowOnHUD = showOnHUD;
        }
        
    }
}

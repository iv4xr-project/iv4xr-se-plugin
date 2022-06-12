using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Gui;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class InfoTab : AbstractScreen<MyGuiScreenTerminal, TerminalInfoData>, IInfoTab
    {
        public override TerminalInfoData Data()
        {
            
            return new TerminalInfoData()
            {
            };
        }
    }
}

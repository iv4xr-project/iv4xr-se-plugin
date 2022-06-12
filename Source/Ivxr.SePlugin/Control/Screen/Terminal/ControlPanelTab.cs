using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Gui;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class ControlPanelTab : AbstractScreen<MyGuiScreenTerminal, TerminalControlPanelData>, IControlPanelTab
    {
        public override TerminalControlPanelData Data()
        {
            
            return new TerminalControlPanelData()
            {
            };
        }
    }
}

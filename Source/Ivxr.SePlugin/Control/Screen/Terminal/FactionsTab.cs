using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Gui;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class FactionsTab : AbstractScreen<MyGuiScreenTerminal, TerminalFactionsData>, IFactionsTab
    {
        public override TerminalFactionsData Data()
        {
            
            return new TerminalFactionsData()
            {
            };
        }
    }
}

using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Gui;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class CommsTab : AbstractScreen<MyGuiScreenTerminal, TerminalCommsData>, ICommsTab
    {
        public override TerminalCommsData Data()
        {
            
            return new TerminalCommsData()
            {
            };
        }
    }
}
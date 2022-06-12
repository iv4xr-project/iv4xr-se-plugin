using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Gui;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class GpsTab : AbstractScreen<MyGuiScreenTerminal, TerminalGpsData>, IGpsTab
    {
        public override TerminalGpsData Data()
        {
            
            return new TerminalGpsData()
            {
            };
        }
    }
}
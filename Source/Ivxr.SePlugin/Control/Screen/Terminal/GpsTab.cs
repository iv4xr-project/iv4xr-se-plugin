using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class GpsTab : AbstractTerminalTab<TerminalGpsData>, IGpsTab
    {
        public GpsTab() : base(MyTerminalPageEnum.Gps, "m_controllerGps")
        {
        }

        public override TerminalGpsData Data()
        {
            
            return new TerminalGpsData()
            {
            };
        }
    }
}

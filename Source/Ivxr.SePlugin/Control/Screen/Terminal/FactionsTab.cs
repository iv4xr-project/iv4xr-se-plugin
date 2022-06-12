using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class FactionsTab : AbstractTerminalTab<TerminalFactionsData>, IFactionsTab
    {
        public FactionsTab() : base(MyTerminalPageEnum.Factions, "m_controllerFactions")
        {
        }

        public override TerminalFactionsData Data()
        {
            
            return new TerminalFactionsData()
            {
            };
        }
    }
}

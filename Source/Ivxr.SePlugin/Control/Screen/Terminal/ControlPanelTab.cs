using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class ControlPanelTab : AbstractTerminalTab<TerminalControlPanelData>, IControlPanelTab
    {
        public ControlPanelTab() : base(MyTerminalPageEnum.ControlPanel, "m_controllerControlPanel")
        {
        }

        public override TerminalControlPanelData Data()
        {
            
            return new TerminalControlPanelData()
            {
            };
        }
        
    }
}

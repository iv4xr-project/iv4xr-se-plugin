using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class InfoTab : AbstractTerminalTab<TerminalInfoData>, IInfoTab
    {
        public InfoTab() : base(MyTerminalPageEnum.Info)
        {
        }

        public override TerminalInfoData Data()
        {
            
            return new TerminalInfoData()
            {
            };
        }
    }
}

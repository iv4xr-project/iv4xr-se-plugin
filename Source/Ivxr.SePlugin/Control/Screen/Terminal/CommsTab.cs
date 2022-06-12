using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class CommsTab : AbstractTerminalTab<TerminalCommsData>, ICommsTab
    {
        public CommsTab() : base(MyTerminalPageEnum.Comms)
        {
        }

        public override TerminalCommsData Data()
        {
            return new TerminalCommsData()
            {
            };
        }
    }
}

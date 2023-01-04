using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class HudNotification
    {
        public string Text;
    }
    public class Hud
    {
        public Dictionary<string, float> Stats;
        public List<HudNotification> Notifications;
    }
}

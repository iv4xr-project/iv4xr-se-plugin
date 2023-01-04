using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Blocks;
using Sandbox.Game.Entities.Cube;
using VRage.Game.GUI.TextPanel;
using VRage.Sync;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class BeaconAdmin : AbstractBlockAdmin<MyBeacon>, IBeaconAdmin
    {
        public BeaconAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetRadius(string blockId, float radius)
        {
            BlockById(blockId).GetInstanceFieldOrThrow<Sync<float, SyncDirection.BothWays>>("m_radius").Value = radius;
        }

        public void SetHudText(string blockId, string text)
        {
            BlockById(blockId).CallMethod<object>("SetHudText", new object[] { text });
        }
    }
}

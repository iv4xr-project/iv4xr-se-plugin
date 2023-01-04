using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using SpaceEngineers.Game.Entities.Blocks;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class SurvivalKitAdmin : AbstractBlockAdmin<MySurvivalKit>, ISurvivalKitAdmin
    {
        public SurvivalKitAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetSpawnName(string blockId, string name)
        {
            BlockById(blockId).CallMethod<object>("SetSpawnName", new object[] { new StringBuilder(name) });
        }

   }
}

using Sandbox.Game.Multiplayer;
using Sandbox.Game.World;

namespace Iv4xr.SePlugin
{
    public static class MyExtensions
    {
        public static bool IsAdminOrCreative(this MySession session)
        {
            // copied from MyCubePlacer.Shoot
            return session.CreativeMode || (session.CreativeToolsEnabled(Sync.MyId) && session.HasCreativeRights);
        }
    }
}
using System;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using VRageMath;

namespace Iv4xr.SePlugin
{
    public static class MyExtensions
    {
        public static PlainVec3D ToPlain(this Vector3D vector)
        {
            return new PlainVec3D(vector.X, vector.Y, vector.Z);
        }

        public static PlainVec3D ToPlain(this Vector3 vector)
        {
            return new PlainVec3D(vector.X, vector.Y, vector.Z);
        }
        
        public static PlainVec3F ToPlainF(this Vector3 vector)
        {
            return new PlainVec3F(vector.X, vector.Y, vector.Z);
        }

        public static PlainVec3I ToPlain(this Vector3I vector)
        {
            return new PlainVec3I(vector.X, vector.Y, vector.Z);
        }
        
        public static bool IsAdminOrCreative(this MySession session)
        {
            // copied from MyCubePlacer.Shoot
            return session.CreativeMode || (session.CreativeToolsEnabled(Sync.MyId) && session.HasCreativeRights);
        }

        /// <summary>
        /// If the page number is negative, it's ignored.
        /// </summary>
        public static void SwitchToPageOrNot(this MyToolbar toolbar, int page)
        {
            if (page < 0)
                return;
 
            toolbar.SwitchToPage(page);
        }
    }
}

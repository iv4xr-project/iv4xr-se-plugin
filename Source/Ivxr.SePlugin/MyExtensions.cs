using System;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.Screens.Helpers;
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

        /// <summary>
        /// If the page number is negative, it's ignored.
        /// </summary>
        public static void SwitchToPageOrNot(this MyToolbar toolbar, int page)
        {
            if (page < 0)
                return;
 
            toolbar.SwitchToPage(page);
        }
        
        [Obsolete("Remove when removing the old protocol")]
        public static void Interact(this Items items, InteractionArgs args)
        {
            if (args.InteractionType == InteractionType.EQUIP)
            {
                items.EquipToolbarItem(new ToolbarLocation() {Slot = args.Slot, Page = args.Page}, args.AllowSizeChange);
            }
            else if (args.InteractionType == InteractionType.PLACE)
            {
                items.Place();
            }
            else if (args.InteractionType == InteractionType.BEGIN_USE)
            {
                items.BeginUseTool();
            }
            else if (args.InteractionType == InteractionType.END_USE)
            {
                items.EndUseTool();
            }
            else if (args.InteractionType == InteractionType.TOOLBAR_SET)
            {
                items.SetToolbarItem(args.ItemName, new ToolbarLocation() {Slot = args.Slot, Page = args.Page});
            }
            else
            {
                throw new ArgumentException("Unknown or not implemented interaction type.");
            }
        }
    }
}

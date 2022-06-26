using System;
using System.Linq;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Screens.Helpers;
using VRage.Game;
using VRage.ObjectBuilders;

namespace Iv4xr.SePlugin.Control
{
    public class Items : IItems
    {
        private readonly IGameSession m_session;
        private readonly LowLevelObserver m_observer;

        public Items(IGameSession session, LowLevelObserver observer)
        {
            m_session = session;
            m_observer = observer;
        }

        public void Equip(ToolbarLocation toolbarLocation)
        {
            EquipToolbarItem(toolbarLocation, false);
        }

        [Obsolete("Deprecated, will create new api for allowSizeChange. You should use Equip.")]
        public void EquipToolbarItem(ToolbarLocation toolbarLocation, bool allowSizeChange)
        {
            var toolbar = m_session.Character.Toolbar;
            toolbar.SwitchToPageOrNot(toolbarLocation.Page);

            if (!allowSizeChange && toolbar.SelectedSlot.HasValue &&
                (toolbar.SelectedSlot.Value == toolbarLocation.Slot))
                return; // Already set (setting it again would change grid size).

            var toolbarItem = toolbar[toolbarLocation.Slot];
            if (toolbarItem is MyToolbarItemWeapon weapon)
            {
                // Very dirty and fast hack, but works for tools, needs more sophistication.
                m_session.Character.SwitchToWeapon(weapon);
            }
            else
            {
                toolbar.ActivateItemAtSlot(toolbarLocation.Slot);
            }
        }

        public void Activate(ToolbarLocation toolbarLocation)
        {
            var toolbar = m_session.Character.Toolbar;
            toolbar.SwitchToPageOrNot(toolbarLocation.Page);
            toolbar.ActivateItemAtSlot(toolbarLocation.Slot);
        }

        public Toolbar Toolbar()
        {
            var toolbar = MyToolbarComponent.CurrentToolbar;
            return new Toolbar()
            {
                SlotCount = toolbar.SlotCount,
                PageCount = toolbar.PageCount,
                Items = new ToolbarItem[toolbar.ItemCount]
                        .Select((index, item) => GetToolbarItem(toolbar[item]))
                        .ToList()
            };
        }


        private static ToolbarItem GetToolbarItem(MyToolbarItem myToolbarItem)
        {
            if (!(myToolbarItem is MyToolbarItemDefinition definition)) return null;
            return definition.ToToolbarItem();
        }

        public void SetToolbarItem(DefinitionId definitionId, ToolbarLocation toolbarLocation)
        {
            var myDefinitionId = definitionId.ToMyDefinitionId();
            Definitions.CheckDefinitionIdExists(myDefinitionId);
            if (IsWeapon(definitionId.Type))
            {
                SetToolbarItem<MyObjectBuilder_ToolbarItemWeapon>(myDefinitionId, toolbarLocation);
            }
            else
            {
                SetToolbarItem<MyObjectBuilder_ToolbarItemCubeBlock>(myDefinitionId, toolbarLocation);
            }
        }

        private bool IsWeapon(string itemName)
        {
            return MyDefinitionManager.Static
                    .GetWeaponDefinitions()
                    .Any(definition => definition.Id.SubtypeName == itemName);
        }

        private void SetToolbarItem<T>(MyDefinitionId id, ToolbarLocation toolbarLocation)
                where T : MyObjectBuilder_ToolbarItemDefinition, new()
        {
            var toolbarItemBuilder = MyObjectBuilderSerializer.CreateNewObject<T>();
            toolbarItemBuilder.DefinitionId = id;

            var toolbar = m_session.Character.Toolbar;
            toolbar.SwitchToPage(toolbarLocation.Page);
            var item = MyToolbarItemFactory.CreateToolbarItem(toolbarItemBuilder);
            toolbar.SetItemAtSlot(toolbarLocation.Slot, item);
        }
    }
}

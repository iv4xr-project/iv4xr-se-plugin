using System;
using System.Linq;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
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

        public void BeginUsingTool()
        {
            var entityController = GetEntityController();
            entityController.ControlledEntity.BeginShoot(MyShootActionEnum.PrimaryAction);
        }

        public void EndUsingTool()
        {
            var entityController = GetEntityController();
            entityController.ControlledEntity.EndShoot(MyShootActionEnum.PrimaryAction);
        }

        public void Equip(ToolbarLocation toolbarLocation)
        {
            EquipToolbarItem(toolbarLocation, false);
        }


        [Obsolete("Deprecated, will create new api for allowSizeChange. Can use Equip.")]
        public void EquipToolbarItem(ToolbarLocation toolbarLocation, bool allowSizeChange)
        {
            var toolbar = MyToolbarComponent.CurrentToolbar;
            toolbar.SwitchToPageOrNot(toolbarLocation.Page);

            if (!allowSizeChange && toolbar.SelectedSlot.HasValue &&
                (toolbar.SelectedSlot.Value == toolbarLocation.Slot))
                return; // Already set (setting it again would change grid size).

            toolbar.ActivateItemAtSlot(toolbarLocation.Slot);
        }

        public Toolbar GetToolbar()
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
            var id = definition.Definition.Id;
            return new ToolbarItem()
            {
                Type = id.TypeId.ToString(),
                SubType = id.SubtypeId.String,
                Name = definition.DisplayName.ToString()
            };
        }

        public void SetToolbarItem(string name, ToolbarLocation toolbarLocation)
        {
            if (IsWeapon(name))
            {
                SetToolbarWeapon(toolbarLocation.Slot, toolbarLocation.Page, name);
            }
            else
            {
                SetToolbarBlock(toolbarLocation.Slot, toolbarLocation.Page, name);
            }
        }

        private bool IsWeapon(string itemName)
        {
            return MyDefinitionManager.Static
                    .GetWeaponDefinitions()
                    .Any(definition => definition.Id.SubtypeName == itemName);
        }

        private void SetToolbarBlock(int slot, int page, string itemName)
        {
            var toolDefinitionId = MyDefinitionManager.Static
                    .GetAllDefinitions()
                    .First(definition => definition.Id.SubtypeName == itemName).Id;
            SetToolbarItem<MyObjectBuilder_ToolbarItemCubeBlock>(slot, page, toolDefinitionId);
        }

        private void SetToolbarWeapon(int slot, int page, string itemName)
        {
            var toolDefinitionId = MyDefinitionManager.Static
                    .GetWeaponDefinitions()
                    .First(definition => definition.Id.SubtypeName == itemName).Id;
            SetToolbarItem<MyObjectBuilder_ToolbarItemWeapon>(slot, page, toolDefinitionId);
        }

        private void SetToolbarItem<T>(int slot, int page, MyDefinitionId id)
                where T : MyObjectBuilder_ToolbarItemDefinition, new()
        {
            var toolbarItemBuilder = MyObjectBuilderSerializer.CreateNewObject<T>();
            toolbarItemBuilder.DefinitionId = id;

            var toolbar = MyToolbarComponent.CurrentToolbar;
            toolbar.SwitchToPage(page);
            toolbar.SetItemAtSlot(slot, MyToolbarItemFactory.CreateToolbarItem(toolbarItemBuilder));
        }

        private MyEntityController GetEntityController()
        {
            if (m_session.Character is null)
                throw new NullReferenceException("I'm out of character!"); // Should not happen.

            var entityController = m_session.Character.ControllerInfo.Controller;

            if (entityController is null) // Happens when the character enters a vehicle, for example.
                throw new NotSupportedException("Entity control not possible now.");

            return entityController;
        }
    }
}

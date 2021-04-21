using System;
using System.Linq;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using VRage.Game;
using VRage.ObjectBuilders;

namespace Iv4xr.SePlugin.Control
{
    public interface IItems
    {
        void PlaceBlock();

        void BeginUseTool();

        void EndUseTool();

        void EquipToolbarItem(int slot, int page);

        void SetToolbarItem(int slot, int page, string itemName);

        Toolbar GetToolbar();
        
        void Interact(InteractionArgs args);
        
    }
    
    public class Items: IItems
    {
        private readonly IGameSession m_session;

        public Items(IGameSession session)
        {
            m_session = session;
        }
        
        public void Interact(InteractionArgs args)
        {
            if (args.InteractionType == InteractionType.EQUIP)
            {
                EquipToolbarItem(args.Slot, args.Page, args.AllowSizeChange);
            }
            else if (args.InteractionType == InteractionType.PLACE)
            {
                PlaceBlock();
            }
            else if (args.InteractionType == InteractionType.BEGIN_USE)
            {
                BeginUseTool();
            }
            else if (args.InteractionType == InteractionType.END_USE)
            {
                EndUseTool();
            }
            else if (args.InteractionType == InteractionType.TOOLBAR_SET)
            {
                SetToolbarItem(args.Slot, args.Page, args.ItemName);
            }
            else
            {
                throw new ArgumentException("Unknown or not implemented interaction type.");
            }
        }
        
        
        public void BeginUseTool()
        {
            var entityController = GetEntityController();
            entityController.ControlledEntity.BeginShoot(MyShootActionEnum.PrimaryAction);
        }

        public void EndUseTool()
        {
            var entityController = GetEntityController();
            entityController.ControlledEntity.EndShoot(MyShootActionEnum.PrimaryAction);
        }

        public void PlaceBlock()
        {
            if (MySession.Static.IsAdminOrCreative())
            {
                if (MyCubeBuilder.Static is null)
                    throw new NullReferenceException("Cube builder is null.");

                MyCubeBuilder.Static.Add();
                return;
            }

            // else: use tool's primary action in the survival mode
            var entityController = GetEntityController();
            entityController.ControlledEntity.BeginShoot(MyShootActionEnum.PrimaryAction);
        }

        public void EquipToolbarItem(int slot, int page)
        {
            EquipToolbarItem(slot, page, false);
        }

        private void EquipToolbarItem(int slot, int page, bool allowSizeChange)
        {
            var toolbar = MyToolbarComponent.CurrentToolbar;
            toolbar.SwitchToPageOrNot(page);

            if (!allowSizeChange && toolbar.SelectedSlot.HasValue && (toolbar.SelectedSlot.Value == slot))
                return; // Already set (setting it again would change grid size).
            
            toolbar.ActivateItemAtSlot(slot);
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

        public void SetToolbarItem(int slot, int page, string itemName)
        {
            if (IsWeapon(itemName))
            {
                SetToolbarWeapon(slot, page, itemName);
            }
            else
            {
                SetToolbarBlock(slot, page, itemName);
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

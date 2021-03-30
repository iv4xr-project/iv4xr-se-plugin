using System;
using System.Linq;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using VRage.Game;
using VRage.ObjectBuilders;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public interface ICharacterController
    {
        void Move(Vector3 move, Vector2 rotation, float roll);
        void Move(MoveAndRotateArgs args);
        void Interact(InteractionArgs args);
    }

    public class CharacterController : ICharacterController
    {
        private readonly IGameSession m_session;

        public CharacterController(IGameSession session)
        {
            m_session = session;
        }

        public void Move(MoveAndRotateArgs args)
        {
            Move(args.Movement, args.Rotation, (float) args.Roll);
        }

        public void Move(Vector3 movement, Vector2 rotation, float roll)
        {
            var entityController = GetEntityController();

            entityController.ControlledEntity.MoveAndRotate(movement, rotation, roll);
        }

        public void Interact(InteractionArgs args)
        {
            if (args.InteractionType == InteractionType.EQUIP)
            {
                EquipToolbarItem(args.Slot, args.Page, args.AllowSizeChange);
            }
            else if (args.InteractionType == InteractionType.PLACE)
            {
                PlaceItem();
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

        public void PlaceItem()
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

        public void EquipToolbarItem(int slot, int page, bool allowSizeChange)
        {
            var toolbar = MyToolbarComponent.CurrentToolbar;
            toolbar.SwitchToPageOrNot(page);

            if (!allowSizeChange && toolbar.SelectedSlot.HasValue && (toolbar.SelectedSlot.Value == slot))
                return; // Already set (setting it again would change grid size).
            
            toolbar.ActivateItemAtSlot(slot);
        }

        /// <summary>
        /// First version. Only tools and weapons are supported for now.
        /// </summary>
        private static void SetToolbarItem(int slot, int page, string itemName)
        {
            var toolDefinitions = MyDefinitionManager.Static.GetWeaponDefinitions();
            var toolDefinition = toolDefinitions.First(definition => definition.Id.SubtypeName == itemName);
            
            var toolbarItemBuilder = MyObjectBuilderSerializer.CreateNewObject<MyObjectBuilder_ToolbarItemWeapon>();
            toolbarItemBuilder.DefinitionId = toolDefinition.Id;

            var item = MyToolbarItemFactory.CreateToolbarItem(toolbarItemBuilder);

            var toolbar = MyToolbarComponent.CurrentToolbar;
            toolbar.SwitchToPageOrNot(page);
            toolbar.SetItemAtSlot(slot, item);
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
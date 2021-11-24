using System.Linq;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Character.Components;
using Sandbox.Game.Weapons;
using Sandbox.Game.World;
using VRage.Game;
using VRage.Game.Entity;

namespace Iv4xr.SePlugin.Control
{
    public class CharacterObservationBuilder
    {

        private readonly EntityBuilder m_entityBuilder;

        internal CharacterObservationBuilder(EntityBuilder entityBuilder)
        {
            m_entityBuilder = entityBuilder;
        }
        
        public CharacterObservation CreateCharacterObservation(MyCharacter character)
        {
            var orientation = character.PositionComp.GetOrientation();
            return new CharacterObservation
            { 
                Id = character.EntityId.ToString(),
                Position = character.PositionComp.GetPosition().ToPlain(), // Consider reducing allocations.
                OrientationForward = orientation.Forward.ToPlain(),
                OrientationUp = orientation.Up.ToPlain(),
                Velocity = character.Physics.LinearVelocity.ToPlain(),
                Extent = character.PositionComp.LocalAABB.Size.ToPlain(),
                Camera = new Pose()
                {
                    Position = MySector.MainCamera.Position.ToPlain(),
                    OrientationForward = MySector.MainCamera.ForwardVector.ToPlain(),
                    OrientationUp = MySector.MainCamera.UpVector.ToPlain(),
                },
                JetpackRunning = character.JetpackComp.TurnedOn,
                HelmetEnabled = character.OxygenComponent.HelmetEnabled,
                Health = character.StatComp.HealthRatio,
                Oxygen = character.GetSuitGasFillLevel(MyCharacterOxygenComponent.OxygenId),
                Hydrogen = character.GetSuitGasFillLevel(MyCharacterOxygenComponent.HydrogenId),
                SuitEnergy = character.SuitEnergyLevel,
                HeadLocalXAngle = character.HeadLocalXAngle,
                HeadLocalYAngle = character.HeadLocalYAngle,
                TargetBlock = TargetBlock(character),
                TargetUseObject = UseObject(character),
                Movement = (CharacterMovementEnum)character.CurrentMovementState,
                Inventory = GetInventory(character.GetInventory()),
                BootsState = GetBootState(character),
            };
        }
        
        private static BootsState GetBootState(MyCharacter character)
        {
            return (BootsState)character
                    .GetInstanceField("m_bootsState").GetInstanceField("m_value");
        }
        
        private Block TargetBlock(MyCharacter character)
        {
            return TargetWeaponBlock(character) ?? TargetDetectorBlock(character);
        }

        private Block TargetWeaponBlock(MyCharacter character)
        {
            if (!(character.CurrentWeapon is MyEngineerToolBase wp)) return null;
            var slimBlock = wp.GetTargetBlock();
            return slimBlock == null ? null : m_entityBuilder.CreateGridBlock(slimBlock);
        }

        private Block TargetDetectorBlock(MyCharacter character)
        {
            var detector = character.Components.Get<MyCharacterDetectorComponent>();
            return detector?.UseObject?.Owner is MyCubeBlock block
                    ? m_entityBuilder.CreateGridBlock(block.SlimBlock)
                    : null;
        }

        private UseObject UseObject(MyCharacter character)
        {
            var detector = character.Components.Get<MyCharacterDetectorComponent>();
            return detector?.UseObject != null ? EntityBuilder.CreateUseObject(detector.UseObject) : null;
        }
        
        
        private InventoryItem GetInventoryItem(MyPhysicalInventoryItem myItem)
        {
            return new InventoryItem()
            {
                Amount = (int)myItem.Amount,
                Id = myItem.Content.GetId().ToDefinitionId(),
            };
        }

        private Inventory GetInventory(MyInventory myInventory)
        {
            return new Inventory()
            {
                CurrentMass = (float)myInventory.CurrentMass,
                CurrentVolume = (float)myInventory.CurrentVolume,
                MaxMass = (float)myInventory.MaxMass,
                MaxVolume = (float)myInventory.MaxVolume,
                CargoPercentage = myInventory.CargoPercentage,
                Items = myInventory.GetItems().Select(GetInventoryItem).ToList(),
            };
        }
    }
}

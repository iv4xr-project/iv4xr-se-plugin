using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Character.Components;
using Sandbox.Game.GameSystems;
using Sandbox.Game.Weapons;
using Sandbox.Game.World;

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
            var result = new CharacterObservation
            {
                Gravity = character.Physics.Gravity.ToPlain(),
                Extent = character.PositionComp.LocalAABB.Size.ToPlain(),
                Camera = new Pose()
                {
                    Position = MySector.MainCamera.Position.ToPlain(),
                    OrientationForward = MySector.MainCamera.ForwardVector.ToPlain(),
                    OrientationUp = MySector.MainCamera.UpVector.ToPlain(),
                },
                JetpackRunning = character.JetpackComp.TurnedOn,
                DampenersOn = character.JetpackComp.DampenersEnabled,
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
                Inventory = character.GetInventory().ToInventory(),
                BootsState = GetBootState(character),
                RelativeDampeningEntity = character.RelativeDampeningEntity.ToEntityOrNull(),
                MovementFlags = (CharacterMovementFlags)((byte)character.MovementFlags),
                JetpackControlThrust = character.JetpackComp.GetInstanceProperty<MyEntityThrustComponent>("ThrustComp").ControlThrust.ToPlain(),
                JetpackFinalThrust = character.JetpackComp.FinalThrust.ToPlain(),
            };
            character.ToEntity(result);
            result.Id = character.CharacterId().ToString();
            return result;
        }

        private static BootsState GetBootState(MyCharacter character)
        {
            return character
                    .GetInstanceFieldOrThrow<object>("m_bootsState")
                    .GetInstanceFieldOrThrow<BootsState>("m_value");
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
    }
}

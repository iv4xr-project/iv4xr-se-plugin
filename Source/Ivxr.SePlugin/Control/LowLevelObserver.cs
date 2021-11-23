using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SePlugin.Config;
using Sandbox.Game;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Character.Components;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Weapons;
using Sandbox.Game.World;
using VRage.Game;
using VRage.Game.Entity;
using VRage.Sync;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class LowLevelObserver
    {
        public ILog Log { get; set; }

        public double Radius
        {
            get => m_radius;
            set
            {
                ConfigValidator.ValidateRadius(value);
                m_radius = value;
            }
        }

        private double m_radius = PluginConfig.DEFAULT_RADIUS;

        private readonly IGameSession m_gameSession;

        private readonly PlainVec3D
                m_agentExtent = new PlainVec3D(0.5, 1, 0.5); // TODO(PP): It's just a quick guess, check the reality.

        private readonly EntityBuilder m_entityBuilder;

        public LowLevelObserver(IGameSession gameSession)
        {
            m_gameSession = gameSession;
            m_entityBuilder = new EntityBuilder() {Log = Log};
        }

        private MyCharacter Character => m_gameSession.Character;

        internal Vector3D GetPlayerPosition()
        {
            return Character.PositionComp.GetPosition();
        }

        private Vector3D GetPlayerVelocity()
        {
            return MySession.Static.ControlledEntity.Entity.Physics.LinearVelocity;
        }

        private InventoryItem GetInventoryItem(MyPhysicalInventoryItem myItem)
        {
            return new InventoryItem()
            {
                Amount = (int) myItem.Amount,
                Id = myItem.Content.GetId().ToDefinitionId(),
            };
        }

        private Inventory GetInventory(MyInventory myInventory)
        {
            return new Inventory()
            {
                CurrentMass = (float) myInventory.CurrentMass,
                CurrentVolume = (float) myInventory.CurrentVolume,
                MaxMass = (float) myInventory.MaxMass,
                MaxVolume = (float) myInventory.MaxVolume,
                CargoPercentage = myInventory.CargoPercentage,
                Items = myInventory.GetItems().Select(GetInventoryItem).ToList(),
            };
        }

        public CharacterObservation GetCharacterObservation()
        {
            var orientation = Character.PositionComp.GetOrientation();
            return new CharacterObservation
            {
                Id = "se0",
                Position = GetPlayerPosition().ToPlain(), // Consider reducing allocations.
                OrientationForward = orientation.Forward.ToPlain(),
                OrientationUp = orientation.Up.ToPlain(),
                Velocity = GetPlayerVelocity().ToPlain(),
                Extent = PlayerExtent(),
                Camera = new Pose()
                {
                    Position = MySector.MainCamera.Position.ToPlain(),
                    OrientationForward = MySector.MainCamera.ForwardVector.ToPlain(),
                    OrientationUp = MySector.MainCamera.UpVector.ToPlain(),
                },
                JetpackRunning = Character.JetpackComp.TurnedOn,
                HelmetEnabled = Character.OxygenComponent.HelmetEnabled,
                Health = Character.StatComp.HealthRatio,
                Oxygen = Character.GetSuitGasFillLevel(MyCharacterOxygenComponent.OxygenId),
                Hydrogen = Character.GetSuitGasFillLevel(MyCharacterOxygenComponent.HydrogenId),
                SuitEnergy = Character.SuitEnergyLevel,
                HeadLocalXAngle = Character.HeadLocalXAngle,
                HeadLocalYAngle = Character.HeadLocalYAngle,
                TargetBlock = TargetBlock(),
                TargetUseObject = UseObject(),
                Movement = (CharacterMovementEnum) Character.CurrentMovementState,
                Inventory = GetInventory(Character.GetInventory()),
                BootsState = GetBootState(Character),
            };
        }

        private static BootsState GetBootState(MyCharacter character)
        {
            return (BootsState)character
                    .GetInstanceField("m_bootsState").GetInstanceField("m_value");
        }

        private PlainVec3D PlayerExtent()
        {
            return Character.PositionComp.LocalAABB.Size.ToPlain();
        }

        private Block TargetBlock()
        {
            return TargetWeaponBlock() ?? TargetDetectorBlock();
        }

        private Block TargetWeaponBlock()
        {
            if (!(Character.CurrentWeapon is MyEngineerToolBase wp)) return null;
            var slimBlock = wp.GetTargetBlock();
            return slimBlock == null ? null : m_entityBuilder.CreateGridBlock(slimBlock);
        }
        
        private Block TargetDetectorBlock()
        {
            var detector = Character.Components.Get<MyCharacterDetectorComponent>();
            return detector?.UseObject?.Owner is MyCubeBlock block ? m_entityBuilder.CreateGridBlock(block.SlimBlock) : null;
        }

        private UseObject UseObject()
        {
            var detector = Character.Components.Get<MyCharacterDetectorComponent>();
            return detector?.UseObject != null ? EntityBuilder.CreateUseObject(detector.UseObject) : null;
        }

        public Observation GetNewBlocks()
        {
            return new Observation()
            {
                Character = GetCharacterObservation(),
                Grids = CollectSurroundingBlocks(GetBoundingSphere(), ObservationMode.NEW_BLOCKS)
            };
        }

        public Observation GetBlocks()
        {
            return new Observation()
            {
                Character = GetCharacterObservation(),
                Grids = CollectSurroundingBlocks(GetBoundingSphere(), ObservationMode.BLOCKS)
            };
        }

        internal BoundingSphereD GetBoundingSphere()
        {
            return GetBoundingSphere(m_radius);
        }

        internal BoundingSphereD GetBoundingSphere(double radius)
        {
            return new BoundingSphereD(GetPlayerPosition(), radius);
        }

        public HashSet<MySlimBlock> GetBlocksOf(MyCubeGrid grid)
        {
            var foundBlocks = new HashSet<MySlimBlock>();
            var sphere = GetBoundingSphere();
            grid.GetBlocksInsideSphere(ref sphere, foundBlocks);
            return foundBlocks;
        }

        private IEnumerable<MyEntity> EnumerateSurroundingEntities(BoundingSphereD sphere)
        {
            List<MyEntity> entities = MyEntities.GetEntitiesInSphere(ref sphere);

            try
            {
                foreach (MyEntity entity in entities)
                    yield return entity;
            }
            finally
            {
                entities.Clear();
            }
        }

        internal List<CubeGrid> CollectSurroundingBlocks(BoundingSphereD sphere, ObservationMode mode)
        {
            return EnumerateSurroundingEntities(sphere)
                    .OfType<MyCubeGrid>()
                    .Select(grid => m_entityBuilder.CreateSeGrid(grid, sphere, mode)).ToList();
        }

        public MyCubeGrid GetGridContainingBlock(string blockId)
        {
            BoundingSphereD sphere = GetBoundingSphere();
            return EnumerateSurroundingEntities(sphere)
                    .OfType<MyCubeGrid>().ToList().FirstOrDefault(grid =>
                    {
                        return GetBlocksOf(grid).FirstOrDefault(block => block.UniqueId.ToString() == blockId) !=
                               null;
                    });
        }

        public MySlimBlock GetBlockByIdOrNull(string blockId)
        {
            var grid = GetGridContainingBlock(blockId);
            return grid == null ? null : GetBlocksOf(grid).FirstOrDefault(b => b.UniqueId.ToString() == blockId);
        }
        
        public MySlimBlock GetBlockById(string blockId)
        {
            var block = GetBlockByIdOrNull(blockId);
            if (block == null)
            {
                throw new ArgumentException("block not found");
            }

            return block;
        }
    }
}

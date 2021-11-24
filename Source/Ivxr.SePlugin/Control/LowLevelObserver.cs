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
        private readonly CharacterObservationBuilder m_characterBuilder;

        public LowLevelObserver(IGameSession gameSession)
        {
            m_gameSession = gameSession;
            m_entityBuilder = new EntityBuilder() { Log = Log };
            m_characterBuilder = new CharacterObservationBuilder(m_entityBuilder);
        }

        private MyCharacter Character => m_gameSession.Character;

        internal Vector3D CurrentPlayerPosition()
        {
            return Character.PositionComp.GetPosition();
        }

        public CharacterObservation GetCharacterObservation()
        {
            return m_characterBuilder.CreateCharacterObservation(Character);
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
            return new BoundingSphereD(CurrentPlayerPosition(), radius);
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

        internal IEnumerable<CharacterObservation> CollectSurroundingCharacters(BoundingSphereD sphere)
        {
            return EnumerateSurroundingEntities(sphere).OfType<MyCharacter>().Select(
                c => m_characterBuilder.CreateCharacterObservation(c));
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
                        return GetBlocksOf(grid).FirstOrDefault(block => block.FatBlock.EntityId.ToString() == blockId) !=
                               null;
                    });
        }

        public MySlimBlock GetBlockByIdOrNull(string blockId)
        {
            var grid = GetGridContainingBlock(blockId);
            return grid == null ? null : GetBlocksOf(grid).FirstOrDefault(b => b.FatBlock.EntityId.ToString() == blockId);
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

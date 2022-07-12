using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Config;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.World;
using VRage.Game.Entity;
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

        private double m_radius = PluginConfigDefaults.RADIUS;

        private readonly IGameSession m_gameSession;

        private readonly PlainVec3D
                m_agentExtent = new PlainVec3D(0.5, 1, 0.5); // TODO(PP): It's just a quick guess, check the reality.

        private readonly EntityBuilder m_entityBuilder;
        private readonly CharacterObservationBuilder m_characterBuilder;

        public LowLevelObserver(IGameSession gameSession)
        {
            m_gameSession = gameSession;
            m_entityBuilder = new EntityBuilder() { Log = Log };
            m_characterBuilder = new CharacterObservationBuilder(m_entityBuilder.BlockEntityBuilder);
        }

        private MyCharacter Character => m_gameSession.Character;

        internal Vector3D CurrentPlayerPosition() => Character.PositionComp.GetPosition();

        internal Vector3D CurrentPlayerOrientationUp() => Character.PositionComp.GetOrientation().Up;

        public CharacterObservation GetCharacterObservation()
        {
            return m_characterBuilder.CreateCharacterObservation(Character);
        }

        public Entity GetEntityObservation()
        {
            if (MySession.Static.ControlledEntity is MyEntity ce)
            {
                return ce.ToEntity();
            }
            return MySession.Static.ControlledEntity?.Entity?.ToEntityOrNull() ?? Character.ToEntity();
        }

        public Observation GetNewBlocks(Vector3? position = null)
        {
            return new Observation()
            {
                Character = GetCharacterObservation(),
                Grids = CollectSurroundingBlocks(GetBoundingSphere(position), ObservationMode.NEW_BLOCKS)
            };
        }

        public Observation GetBlocks(Vector3D? position = null)
        {
            return new Observation()
            {
                Character = GetCharacterObservation(),
                Grids = CollectSurroundingBlocks(GetBoundingSphere(position), ObservationMode.BLOCKS)
            };
        }

        internal BoundingSphereD GetBoundingSphere(Vector3D? position = null)
        {
            return GetBoundingSphere(position, m_radius);
        }

        internal BoundingSphereD GetBoundingSphere(Vector3D? position, double radius)
        {
            return new BoundingSphereD(position ?? CurrentPlayerPosition(), radius);
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

        public List<FloatingObject> ObserveFloatingObjects(Vector3D? position = null)
        {
            return SurroundingFloatingObjects(GetBoundingSphere(position)).ToList();
        }

        internal IEnumerable<FloatingObject> SurroundingFloatingObjects(BoundingSphereD sphere)
        {
            return EnumerateSurroundingEntities(sphere).OfType<MyFloatingObject>()
                    .Select(mfo => mfo.ToFloatingObject());
        }

        internal IEnumerable<CharacterObservation> CollectSurroundingCharacters(BoundingSphereD sphere)
        {
            return EnumerateSurroundingEntities(sphere).OfType<MyCharacter>().Select(
                c => m_characterBuilder.CreateCharacterObservation(c));
        }

        public List<CharacterObservation> ObserveCharacters(Vector3D? position = null)
        {
            return CollectSurroundingCharacters(GetBoundingSphere(position)).ToList();
        }

        public List<CharacterObservation> AllCharacters()
        {
            return Sync.Players.GetOnlinePlayers()
                    .Where(p => p.Character != null)
                    .Select(p => p.Character)
                    .Select(c =>
                            m_characterBuilder.CreateCharacterObservation(c)
                    )
                    .ToList();
        }

        internal List<CubeGrid> CollectSurroundingBlocks(BoundingSphereD sphere, ObservationMode mode)
        {
            return EnumerateSurroundingEntities(sphere)
                    .OfType<MyCubeGrid>()
                    .Select(grid => m_entityBuilder.CreateSeGrid(grid, sphere, mode)).ToList();
        }

        internal CubeGrid ConvertToSeGrid(MyCubeGrid sourceGrid, BoundingSphereD sphere)
        {
            return m_entityBuilder.CreateSeGrid(sourceGrid, sphere, ObservationMode.BLOCKS);
        }

        public IEnumerable<MyCubeGrid> Grids()
        {
            BoundingSphereD sphere = GetBoundingSphere();
            return EnumerateSurroundingEntities(sphere)
                    .OfType<MyCubeGrid>();
        }

        public MyCubeGrid GetGridById(string gridId)
        {
            return (MyCubeGrid) MyEntities.GetEntityById(long.Parse(gridId));
        }

        public MyCubeGrid GetGridContainingBlock(string blockId)
        {
            BoundingSphereD sphere = GetBoundingSphere();
            return EnumerateSurroundingEntities(sphere)
                    .OfType<MyCubeGrid>().ToList().FirstOrDefault(grid =>
                    {
                        return GetBlocksOf(grid).FirstOrDefault(block => block.BlockId().ToString() == blockId) !=
                               null;
                    });
        }

        public MySlimBlock GetBlockByIdOrNull(string blockId)
        {
            var grid = GetGridContainingBlock(blockId);
            return grid == null ? null : GetBlocksOf(grid).FirstOrDefault(b => b.BlockId().ToString() == blockId);
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

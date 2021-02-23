using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Cube;
using VRage.Game.Entity;
using VRage.Game.ModAPI;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    internal class BlockConverter
    {
        public SeBlock CreateSeBlock(MySlimBlock sourceBlock)
        {
            var grid = sourceBlock.CubeGrid;

            return new SeBlock
            {
                    Position = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Position)),
                    MaxIntegrity = sourceBlock.MaxIntegrity,
                    BuildIntegrity = sourceBlock.BuildIntegrity,
                    Integrity = sourceBlock.Integrity,
                    BlockType = GetSeBlockType(sourceBlock),
                    MinPosition = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Min)),
                    MaxPosition = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Max)),

                    // Note: it does not have to be the same as block.Min - block.Max (because of rotations)
                    Size = new PlainVec3D(sourceBlock.BlockDefinition.Size),

                    OrientationForward =
                            new PlainVec3D(grid.WorldMatrix.GetDirectionVector(sourceBlock.Orientation.Forward)),
                    OrientationUp = new PlainVec3D(grid.WorldMatrix.GetDirectionVector(sourceBlock.Orientation.Up))
            };
        }

        private static string GetSeBlockType(IMySlimBlock sourceBlock)
        {
            // block.BlockDefinition.Id.TypeId not used, SubtypeName seems sufficiently unique for now.
            return sourceBlock.BlockDefinition?.Id is null
                    ? "null"
                    : sourceBlock.BlockDefinition.Id.SubtypeName;
        }
    }

    internal class LowLevelObserver
    {
        public ILog Log { get; set; }

        private readonly GameSession m_gameSession;

        private readonly PlainVec3D
                m_agentExtent = new PlainVec3D(0.5, 1, 0.5); // TODO(PP): It's just a quick guess, check the reality.

        private readonly BlockConverter m_blockConverter;

        private HashSet<int> m_previousBlockIds = new HashSet<int>();

        public LowLevelObserver(GameSession gameSession)
        {
            m_gameSession = gameSession;
            m_blockConverter = new BlockConverter();
        }

        private MyCharacter Character => m_gameSession.Character;

        public Vector3D GetPlayerPosition()
        {
            return Character.PositionComp.GetPosition();
        }

        public Vector3D GetPlayerVelocity()
        {
            // TODO(PP): Calculate velocity!
            return Vector3D.Zero;
        }

        public SeObservation GetBasicObservation()
        {
            var characterPosition = GetPlayerPosition();

            var orientation = Character.PositionComp.GetOrientation();

            var observation = new SeObservation
            {
                    AgentID = "se0",
                    Position = new PlainVec3D(characterPosition), // Consider reducing allocations.
                    OrientationForward = new PlainVec3D(orientation.Forward),
                    OrientationUp = new PlainVec3D(orientation.Up),
                    Velocity = new PlainVec3D(GetPlayerVelocity()),
                    Extent = m_agentExtent,
            };

            return observation;
        }

        public IEnumerable<MyEntity> EnumerateSurroundingEntities(BoundingSphereD sphere)
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

        public List<SeBlock> CollectSurroundingBlocks(BoundingSphereD sphere, ObservationMode mode)
        {
            var ivBlocks = new List<SeBlock>(); // iv4XR interface blocks ("SE blocks" from the outside)

            var blockIds = new HashSet<int>();

            foreach (MyEntity entity in EnumerateSurroundingEntities(sphere))
            {
                var grid = entity as MyCubeGrid;
                if (grid is null)
                    continue;

                var foundBlocks = new HashSet<MySlimBlock>();
                grid.GetBlocksInsideSphere(ref sphere, foundBlocks); // NOTE: Likely slow.

                foreach (MySlimBlock sourceBlock in foundBlocks)
                {
                    int blockId = sourceBlock.UniqueId;
                    blockIds.Add(blockId); // TODO(PP): Consider not updating the hash set when not in NEW_BLOCKS mode.

                    if (mode == ObservationMode.NEW_BLOCKS)
                    {
                        if (m_previousBlockIds.Contains(blockId))
                            continue;
                    }

                    ivBlocks.Add(m_blockConverter.CreateSeBlock(sourceBlock));

                    if (ivBlocks.Count() > 1000) // TODO(PP): Define as param.
                    {
                        Log?.WriteLine($"{nameof(CollectSurroundingBlocks)}: Too many blocks!");
                        break;
                    }
                }
            }

            m_previousBlockIds = blockIds;

            Log?.WriteLine($"{nameof(CollectSurroundingBlocks)}: Found {ivBlocks.Count} new blocks.");

            return ivBlocks;
        }
    }
}
using System.Collections.Generic;
using System.Linq;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using VRage.Game.ModAPI;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    internal class SeEntityBuilder
    {
        private HashSet<int> m_previousBlockIds = new HashSet<int>();

        public SeGrid CreateSeGrid(MyCubeGrid sourceGrid, BoundingSphereD sphere)
        {
            var seBlocks = GridBLocks(FoundBlocks(sourceGrid, sphere)).ToList();
            var position = sourceGrid.GridIntegerToWorld(sphere.Center);
            return new SeGrid
            {
                    Position = new PlainVec3D(position),
                    Blocks = seBlocks
            };
        }

        private IEnumerable<SeBlock> GridBLocks(IEnumerable<MySlimBlock> foundBlocks, int limit = 1000)
        {
            var newBlockIds = new HashSet<int>();
            var blocks = foundBlocks.Select(block =>
                    {
                        newBlockIds.Add(block.UniqueId);
                        return block;
                    }).Where(block => !m_previousBlockIds.Contains(block.UniqueId))
                    .Take(limit) //or Where + local variable counting and logging result if previous behaviour desirable
                    .Select(CreateSeBlock);
            m_previousBlockIds = newBlockIds;
            return blocks;
        }

        private static IEnumerable<MySlimBlock> FoundBlocks(MyCubeGrid grid, BoundingSphereD sphere)
        {
            var foundBlocks = new HashSet<MySlimBlock>();
            grid.GetBlocksInsideSphere(ref sphere, foundBlocks);
            return foundBlocks;
        }

        private static SeBlock CreateSeBlock(MySlimBlock sourceBlock)
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
}
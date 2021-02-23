using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using VRage.Game.ModAPI;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    internal class PreviousBlocksFilter
    {
        private readonly HashSet<int> m_previousBlockIds = new HashSet<int>();
        private readonly HashSet<int> m_newBlockIds = new HashSet<int>();

        private bool FilterOnlyNew(MySlimBlock block)
        {
            m_newBlockIds.Add(block.UniqueId);
            return !m_previousBlockIds.Contains(block.UniqueId);
        }

        public Func<MySlimBlock, bool> FilterByMode(ObservationMode mode)
        {
            if (mode == ObservationMode.NEW_BLOCKS)
            {
                return FilterOnlyNew;
            }
            else
            {
                return block => true;
            }
        }

        public void AfterFilter()
        {
            m_previousBlockIds.Clear();
            m_newBlockIds.ForEach(it => m_previousBlockIds.Add(it));
        }
    }

    internal class SeEntityBuilder
    {
        public SeGrid CreateSeGrid(MyCubeGrid sourceGrid, BoundingSphereD sphere, ObservationMode mode)
        {
            var seBlocks = GridBLocks(FoundBlocks(sourceGrid, sphere), mode).ToList();
            var position = sourceGrid.GridIntegerToWorld(sphere.Center);
            return new SeGrid
            {
                    Position = new PlainVec3D(position),
                    Blocks = seBlocks
            };
        }

        private readonly PreviousBlocksFilter m_previousBlocksFilter = new PreviousBlocksFilter();

        private IEnumerable<SeBlock> GridBLocks(IEnumerable<MySlimBlock> foundBlocks, ObservationMode mode,
            int limit = 1000)
        {
            var blocks = foundBlocks.Where(m_previousBlocksFilter.FilterByMode(mode));
            m_previousBlocksFilter.AfterFilter();
            return blocks
                    .Take(limit) //or Where + local variable counting and logging result if previous behaviour desirable
                    .Select(CreateSeBlock);
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
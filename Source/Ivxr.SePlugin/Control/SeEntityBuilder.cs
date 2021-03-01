using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
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

            return block => true;
        }

        public void UpdateAfterFilter()
        {
            m_previousBlockIds.Clear();
            m_newBlockIds.ForEach(it => m_previousBlockIds.Add(it));
        }
    }

    internal class SeEntityBuilder
    {
        public ILog Log { get; set; }

        private readonly int m_blockCountTakeLimit;

        private readonly float m_blockCountWarningRatio;

        public SeEntityBuilder(int blockCountTakeLimit = 10_000, float blockCountWarningRatio = 0.9f)
        {
            m_blockCountTakeLimit = blockCountTakeLimit;
            m_blockCountWarningRatio = blockCountWarningRatio;
        }

        public SeGrid CreateSeGrid(MyCubeGrid sourceGrid, BoundingSphereD sphere, ObservationMode mode)
        {
            var seBlocks = CreateGridBLocks(FoundBlocks(sourceGrid, sphere), mode).ToList();
            var position = sourceGrid.PositionComp.GetPosition();
            
            return new SeGrid
            {
                Position = new PlainVec3D(position),
                Blocks = seBlocks
            };
        }

        private readonly PreviousBlocksFilter m_previousBlocksFilter = new PreviousBlocksFilter();

        private IEnumerable<SeBlock> CreateGridBLocks(IEnumerable<MySlimBlock> foundBlocks, ObservationMode mode)
        {
            var blocks = foundBlocks.Where(m_previousBlocksFilter.FilterByMode(mode));
            m_previousBlocksFilter.UpdateAfterFilter();
            
            var limited = blocks.Take(m_blockCountTakeLimit).ToList();
            
            if (limited.Count * m_blockCountWarningRatio > m_blockCountTakeLimit)
            {
                Log?.WriteLine($"Number of blocks {limited.Count} for grid is reaching or reached limit {m_blockCountTakeLimit}");
            }

            return limited.Select(CreateSeBlock);
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
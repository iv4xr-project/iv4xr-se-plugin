using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using VRage.Game.Entity.UseObject;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    internal class PreviousBlocksFilter
    {
        private readonly HashSet<long> m_previousBlockIds = new HashSet<long>();
        private readonly HashSet<long> m_newBlockIds = new HashSet<long>();

        private bool FilterOnlyNew(MySlimBlock block)
        {
            m_newBlockIds.Add(block.BlockId());
            return !m_previousBlockIds.Contains(block.BlockId());
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

    internal class EntityBuilder
    {
        public ILog Log { get; set; }

        private readonly int m_blockCountTakeLimit;

        private readonly float m_blockCountWarningRatio;

        private readonly BlockEntityBuilder m_blockEntityBuilder = new BlockEntityBuilder();

        public EntityBuilder(int blockCountTakeLimit = 10_000, float blockCountWarningRatio = 0.9f)
        {
            m_blockCountTakeLimit = blockCountTakeLimit;
            m_blockCountWarningRatio = blockCountWarningRatio;
        }

        public CubeGrid CreateSeGrid(MyCubeGrid sourceGrid, BoundingSphereD sphere, ObservationMode mode)
        {
            var seBlocks = CreateGridBLocks(FoundBlocks(sourceGrid, sphere), mode).ToList();
            var position = sourceGrid.PositionComp.GetPosition();
            var orientationUp = sourceGrid.PositionComp.GetOrientation().Up;
            var orientationForward = sourceGrid.PositionComp.GetOrientation().Forward;
            return new CubeGrid
            {
                Id = sourceGrid.EntityId.ToString(),
                Position = position.ToPlain(),
                OrientationForward = orientationForward.ToPlain(),
                OrientationUp = orientationUp.ToPlain(),
                Blocks = seBlocks,
                Mass = sourceGrid.Mass,
                Parked = sourceGrid.IsParked,
            };
        }

        private readonly PreviousBlocksFilter m_previousBlocksFilter = new PreviousBlocksFilter();

        private IEnumerable<Block> CreateGridBLocks(IEnumerable<MySlimBlock> foundBlocks, ObservationMode mode)
        {
            var blocks = foundBlocks.Where(m_previousBlocksFilter.FilterByMode(mode));
            m_previousBlocksFilter.UpdateAfterFilter();

            var limited = blocks.Take(m_blockCountTakeLimit).ToList();

            if (limited.Count * m_blockCountWarningRatio > m_blockCountTakeLimit)
            {
                Log?.WriteLine(
                    $"Number of blocks {limited.Count} for grid is reaching or reached limit {m_blockCountTakeLimit}");
            }

            return limited.Select(CreateGridBlock);
        }

        private static IEnumerable<MySlimBlock> FoundBlocks(MyCubeGrid grid, BoundingSphereD sphere)
        {
            var foundBlocks = new HashSet<MySlimBlock>();
            grid.GetBlocksInsideSphere(ref sphere, foundBlocks);
            return foundBlocks;
        }

        public static UseObject CreateUseObject(IMyUseObject obj)
        {
            return new UseObject()
            {
                Name = obj.GetType().Name,
                SupportedActions = (int)obj.SupportedActions,
                PrimaryAction = (int)obj.PrimaryAction,
                SecondaryAction = (int)obj.SecondaryAction,
                ContinuousUsage = obj.ContinuousUsage,
            };
        }

        public Block CreateGridBlock(MySlimBlock sourceBlock)
        {
            return m_blockEntityBuilder.CreateAndFill(sourceBlock);
        }

    }
}

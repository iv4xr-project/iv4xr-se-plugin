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
    internal class EntityBuilder
    {
        public ILog Log { get; set; }

        private readonly int m_blockCountTakeLimit;

        private readonly float m_blockCountWarningRatio;

        public readonly BlockEntityBuilder BlockEntityBuilder = new BlockEntityBuilder();

        public EntityBuilder(int blockCountTakeLimit = 10_000, float blockCountWarningRatio = 0.9f)
        {
            m_blockCountTakeLimit = blockCountTakeLimit;
            m_blockCountWarningRatio = blockCountWarningRatio;
        }

        public CubeGrid CreateSeGrid(MyCubeGrid sourceGrid, BoundingSphereD sphere)
        {
            var seBlocks = CreateGridBLocks(FoundBlocks(sourceGrid, sphere)).ToList();
            return CreateSeGrid(sourceGrid, seBlocks);
        }
        
        public static CubeGrid CreateSeGrid(MyCubeGrid sourceGrid, List<Block> blocks)
        {
            var position = sourceGrid.PositionComp.GetPosition();
            var orientationUp = sourceGrid.PositionComp.GetOrientation().Up;
            var orientationForward = sourceGrid.PositionComp.GetOrientation().Forward;
            var result =  new CubeGrid
            {
                Id = sourceGrid.EntityId.ToString(),
                Position = position.ToPlain(),
                OrientationForward = orientationForward.ToPlain(),
                OrientationUp = orientationUp.ToPlain(),
                Blocks = blocks,
                Mass = sourceGrid.Mass,
                Parked = sourceGrid.IsParked,
            };
            sourceGrid.ToEntity(result);
            return result;
        }
        
        public CubeGrid CreateSeGrid(MyCubeGrid sourceGrid)
        {
            return CreateSeGrid(sourceGrid, sourceGrid.CubeBlocks.Select(b => CreateGridBlock(b)).ToList());
        }

        private IEnumerable<Block> CreateGridBLocks(IEnumerable<MySlimBlock> blocks)
        {
            return blocks.Select(CreateGridBlock);
        }

        private static IEnumerable<MySlimBlock> FoundBlocks(MyCubeGrid grid, BoundingSphereD sphere)
        {
            return grid.CubeBlocks;
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
            return BlockEntityBuilder.CreateAndFill(sourceBlock);
        }

    }
}

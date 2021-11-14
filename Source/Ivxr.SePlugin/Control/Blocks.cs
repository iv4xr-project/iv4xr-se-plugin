using System;
using System.Linq;
using System.Reflection;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.World;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control
{
    public class Blocks : IBlocks, IBlocksAdmin
    {
        private readonly IGameSession m_session;
        private readonly LowLevelObserver m_observer;

        public Blocks(IGameSession session, LowLevelObserver observer)
        {
            m_session = session;
            m_observer = observer;
        }

        private readonly BlockPlacer m_blockPlacer = new BlockPlacer();

        public void SetIntegrity(string blockId, float integrity)
        {
            var grid = m_observer.GetGridContainingBlock(blockId);
            if (grid == null) throw new ArgumentException("Block with id not found");
            
            var block = m_observer.GetBlocksOf(grid).FirstOrDefault(b => b.BlockId().ToString() == blockId);
            if (block == null) throw new ArgumentException("Block with id not found");
            
            var method = block.ComponentStack.GetType().GetMethod("SetIntegrity",
                BindingFlags.NonPublic | BindingFlags.Instance);
            if (method == null) throw new InvalidOperationException("Method not found");
            
            method.Invoke(block.ComponentStack, new object[] {integrity, integrity});
            block.UpdateVisual();
        }

        public string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp)
        {
            return m_blockPlacer.PlaceSingleBlock(blockDefinitionId, position.ToVector3(), orientationForward.ToVector3(),
                orientationUp.ToVector3()).BlockId().ToString();
        }

        public string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition, PlainVec3I orientationForward,
            PlainVec3I orientationUp)
        {
            //var playerId = m_session.Character.GetPlayerId();
            var grid = m_observer.GetGridById(gridId);
            return m_blockPlacer.PlaceInGrid(
                blockDefinitionId.ToMyCubeBlockDefinition().Id, grid, minPosition.ToVector3I(), 
                orientationForward.ToVector3I(), orientationUp.ToVector3I(), MySession.Static.LocalPlayerId
            ).UniqueId.ToString();
        }

        public void Remove(string blockId)
        {
            var grid = m_observer.GetGridContainingBlock(blockId);
            if (grid == null)
            {
                throw new ArgumentException("Block with id not found");
            }

            var block = m_observer.GetBlocksOf(grid).FirstOrDefault(b => b.BlockId().ToString() == blockId);
            grid.RemoveBlock(block);
        }

        public void Place()
        {
            if (MySession.Static.IsAdminOrCreative())
            {
                if (MyCubeBuilder.Static is null)
                    throw new NullReferenceException("Cube builder is null.");

                MyCubeBuilder.Static.Add();
                return;
            }

            // else: use tool's primary action in the survival mode
            var entityController = GetEntityController();
            entityController.ControlledEntity.BeginShoot(MyShootActionEnum.PrimaryAction);
        }

        private MyEntityController GetEntityController()
        {
            if (m_session.Character is null)
                throw new NullReferenceException("I'm out of character!"); // Should not happen.

            var entityController = m_session.Character.ControllerInfo.Controller;

            if (entityController is null) // Happens when the character enters a vehicle, for example.
                throw new NotSupportedException("Entity control not possible now.");

            return entityController;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control.Screen.BlockAdmin;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Common.ObjectBuilders;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.GameSystems;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Game.World;
using SpaceEngineers.Game.Entities.Blocks;
using VRage.Utils;

namespace Iv4xr.SePlugin.Control
{
    public class BlocksAdmin : IBlocksAdmin
    {
        private readonly IGameSession m_session;
        private readonly LowLevelObserver m_observer;

        public BlocksAdmin(IGameSession session, LowLevelObserver observer)
        {
            m_session = session;
            m_observer = observer;
            Warhead = new WarheadAdmin(session, observer);
        }

        private readonly BlockPlacer m_blockPlacer = new BlockPlacer();

        public void SetIntegrity(string blockId, float integrity)
        {
            var block = m_observer.GetBlockById(blockId);

            var method = block.ComponentStack.GetType().GetMethod("SetIntegrity",
                BindingFlags.NonPublic | BindingFlags.Instance);
            if (method == null) throw new InvalidOperationException("Method not found");

            method.Invoke(block.ComponentStack, new object[] { integrity, integrity });
            block.UpdateVisual();
        }


        public void SetCustomName(string blockId, string customName)
        {
            var block = m_observer.GetBlockById(blockId);
            switch (block.FatBlock)
            {
                case null:
                    throw new ArgumentException($"Block {blockId} is not Functional");
                case MyTerminalBlock terminalBlock:
                    // DisplayNameText is backed by CustomName, but CustomName setter is private, this is simpler.
                    terminalBlock.DisplayNameText = customName;
                    return;
            }

            throw new ArgumentException($"Block {blockId} is not Terminal block and doesn't have CustomName");
        }

        public void CreateOrUpdateGroup(string name, string gridId, List<string> blockIds)
        {
            var grid = m_observer.GetGridById(gridId);
            var terminalSystem = grid.GridSystems.TerminalSystem;
            var relevantConstructor = typeof(MyBlockGroup).GetConstructors(
                BindingFlags.NonPublic | BindingFlags.Instance).FirstOrDefault();
            var group = (MyBlockGroup)relevantConstructor?.Invoke(new object[] { });
            group.Name.Append(name);
            var blocks = group.GetInstanceFieldOrThrow<HashSet<MyTerminalBlock>>("Blocks");
            blockIds.Select(blockId => m_observer.GetBlockById(blockId).FatBlock as MyTerminalBlock)
                    .ForEach(terminalBlock => blocks.Add(terminalBlock));
            terminalSystem.AddUpdateGroup(group, true, true);
        }

        public void MapButtonToBlock(string buttonBlockId, int buttonIndex, string action, string targetId)
        {
            MapAction(buttonBlockId, buttonIndex, new MyObjectBuilder_ToolbarItemTerminalBlock
            {
                _Action = action,
                BlockEntityId = m_observer.GetBlockById(targetId).FatBlock.EntityId
            });
        }

        public void MapButtonToGroup(string buttonBlockId, int buttonIndex, string action, string groupName)
        {
            var block = m_observer.GetBlockById(buttonBlockId);
            MapAction(buttonBlockId, buttonIndex, new MyObjectBuilder_ToolbarItemTerminalGroup
            {
                _Action = action,
                GroupName = groupName,
                BlockEntityId = ((MyButtonPanel)block.FatBlock).EntityId
            });
        }

        private void MapAction(string buttonBlockId, int buttonIndex,
            MyObjectBuilder_ToolbarItemTerminal data)
        {
            data._Action.ThrowIfNull("_Action", "Action must be set!");
            var block = m_observer.GetBlockById(buttonBlockId);
            var item = MyToolbarItemFactory.CreateToolbarItem(data);
            if (block.FatBlock is MyButtonPanel buttonPanel)
            {
                if (buttonIndex >= buttonPanel.Toolbar.ItemCount || buttonIndex < 0)
                {
                    throw new IndexOutOfRangeException(
                        $"Invalid buttonIndex {buttonIndex}, can be between 0 and {buttonPanel.Toolbar.ItemCount}");
                }

                buttonPanel.Toolbar.SetItemAtIndex(buttonIndex, item);
            }
            else
            {
                throw new InvalidOperationException($"block {buttonBlockId} is not ButtonPanel");
            }
        }

        public string PlaceAt(DefinitionId blockDefinitionId, PlainVec3D position, PlainVec3D orientationForward,
            PlainVec3D orientationUp, PlainVec3F? color = null)
        {
            Definitions.CheckDefinitionIdExistsAndEnabled(blockDefinitionId.ToMyDefinitionId());
            return m_blockPlacer.PlaceSingleBlock(m_session.CurrentCharacterId, blockDefinitionId, position.ToVector3(),
                orientationForward.ToVector3(),
                orientationUp.ToVector3(), color?.ToVector3() ?? MyPlayer.SelectedColor).BlockId().ToString();
        }

        public string PlaceInGrid(DefinitionId blockDefinitionId, string gridId, PlainVec3I minPosition,
            PlainVec3I orientationForward,
            PlainVec3I orientationUp, PlainVec3F? color = null)
        {
            Definitions.CheckDefinitionIdExistsAndEnabled(blockDefinitionId.ToMyDefinitionId());
            var grid = m_observer.GetGridById(gridId);
            return m_blockPlacer.PlaceInGrid(
                blockDefinitionId.ToMyCubeBlockDefinition().Id, grid, minPosition.ToVector3I(),
                orientationForward.ToVector3I(), orientationUp.ToVector3I(), MySession.Static.LocalPlayerId,
                color?.ToVector3(),
                MyStringHash.GetOrCompute(MyPlayer.SelectedArmorSkin)
            ).UniqueId.ToString();
        }

        public IWarheadAdmin Warhead { get; }

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
    }
}

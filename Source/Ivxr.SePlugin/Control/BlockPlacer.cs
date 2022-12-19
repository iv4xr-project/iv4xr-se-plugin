using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.World;
using Sandbox.ModAPI;
using VRage;
using VRage.Game;
using VRage.ObjectBuilders;
using VRage.Utils;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class BlockPlacer
    {
        private MyObjectBuilder_CubeBlock CubeBlockBuilderByBlockType(long ownerId, DefinitionId blockDefinitionId,
            Vector3 colorMask)
        {
            var obj = (MyObjectBuilder_CubeBlock)MyObjectBuilderSerializer.CreateNewObject(blockDefinitionId
                    .ToMyDefinitionId());
            obj.Min = new SerializableVector3I(0, 0, 0);
            obj.SubtypeName = blockDefinitionId.Type;
            obj.BlockOrientation = new SerializableBlockOrientation(Base6Directions.Direction.Forward,
                Base6Directions.Direction.Up);
            obj.Owner = ownerId;
            obj.BuiltBy = ownerId;
            obj.ColorMaskHSV = colorMask;
            return obj;
        }

        private MyCubeGrid PlaceBlock(MyObjectBuilder_CubeBlock block, Vector3D position, Vector3 forward,
            Vector3 up)
        {
            // The block definition is useful for getting the size of the block
            var blockDefinition = MyDefinitionManager.Static.GetCubeBlockDefinition(block);

            // Create a new grid for the block
            var gridBuilder = new MyObjectBuilder_CubeGrid()
            {
                PersistentFlags = MyPersistentEntityFlags2.CastShadows | MyPersistentEntityFlags2.InScene,
                GridSizeEnum = blockDefinition.CubeSize,
                IsStatic = true,
                PositionAndOrientation = new MyPositionAndOrientation(position, forward, up),
                DisplayName = "Grid",
            };

            // Add the block to the grid
            gridBuilder.CubeBlocks.Add(block);

            // Create the grid (not sure if all the lines below are required)
            MyAPIGateway.Entities.RemapObjectBuilder(gridBuilder);
            MyCubeGrid entity = (MyCubeGrid)MyAPIGateway.Entities.CreateFromObjectBuilderAndAdd(gridBuilder);
            MyAPIGateway.Multiplayer.SendEntitiesCreated(new List<MyObjectBuilder_EntityBase> { gridBuilder });


            // Return the created entity
            return entity;
        }

        public MySlimBlock PlaceInGrid(
            MyDefinitionId blockDefinitionId,
            MyCubeGrid currentGrid,
            Vector3I min,
            Vector3I orientationForward,
            Vector3I orientationUp,
            long playerId,
            Vector3? colorRgb,
            MyStringHash skinId
        )
        {
            var blocksBuildQueue = new HashSet<MyCubeGrid.MyBlockLocation>();
            var orientation = Quaternion.CreateFromForwardUp(orientationForward, orientationUp);
            var myBlockLocation = new MyCubeGrid.MyBlockLocation(
                blockDefinitionId, min, min, min, orientation, MyEntityIdentifier.AllocateId(),
                playerId
            );
            blocksBuildQueue.Add(myBlockLocation);
            var newBlocks = new List<MySlimBlock>();
            var callback = new Action<MyCubeGrid, MySlimBlock>((grid, block) => { newBlocks.Add(block); });
            MyCubeGrids.BlockBuilt += callback;
            currentGrid.BuildBlocks(colorRgb?.RgbToHsv() ?? MyPlayer.SelectedColor, skinId,
                blocksBuildQueue, MySession.Static.LocalCharacterEntityId, MySession.Static.LocalPlayerId);
            MyCubeGrids.BlockBuilt -= callback;
            if (newBlocks.Count == 0)
            {
                throw new InvalidOperationException(
                    $"Couldn't place the block {blockDefinitionId} at {min} to grid {currentGrid.EntityId}.");
            }

            if (newBlocks.Count > 1)
            {
                throw new InvalidOperationException("Built more than one block!");
            }

            return newBlocks.First();
        }

        public MyCubeGrid PlaceSingleBlock(long ownerId, DefinitionId blockDefinitionId, Vector3 position,
            Vector3 orientationForward,
            Vector3 orientationUp, Vector3? colorRgb)
        {
            var grid = PlaceBlock(
                CubeBlockBuilderByBlockType(ownerId, blockDefinitionId, colorRgb?.RgbToHsv() ?? MyPlayer.SelectedColor),
                position, orientationForward,
                orientationUp);
            return grid;
        }

        public List<MySlimBlock> PlaceInGrid(
            MyCubeGrid currentGrid,
            List<BlockLocation> blockLocations,
            long playerId,
            Vector3? colorRgb,
            MyStringHash skinId
        )
        {
            var blocksBuildQueue = new HashSet<MyCubeGrid.MyBlockLocation>(blockLocations.Select(
                bl => bl.ToMyBlockLocation(MyEntityIdentifier.AllocateId(), playerId)
            ));

            var newBlocksByGridPositions = new Dictionary<Vector3I, MySlimBlock>();
            var callback = new Action<MyCubeGrid, MySlimBlock>((grid, block) =>
            {
                newBlocksByGridPositions.Add(block.Position, block);
            });
            MyCubeGrids.BlockBuilt += callback;
            currentGrid.BuildBlocks(colorRgb?.RgbToHsv() ?? MyPlayer.SelectedColor, skinId,
                blocksBuildQueue, MySession.Static.LocalCharacterEntityId, MySession.Static.LocalPlayerId);
            MyCubeGrids.BlockBuilt -= callback;
            if (newBlocksByGridPositions.Count == 0)
            {
                throw new InvalidOperationException(
                    $"Couldn't place blocks to grid {currentGrid.EntityId}.");
            }

            if (newBlocksByGridPositions.Count != blockLocations.Count)
            {
                throw new InvalidOperationException("Built more than one block!");
            }

            return blockLocations.Select(
                bl =>
                        newBlocksByGridPositions[bl.MinPosition.ToVector3I()]
            ).ToList();
        }
    }
}

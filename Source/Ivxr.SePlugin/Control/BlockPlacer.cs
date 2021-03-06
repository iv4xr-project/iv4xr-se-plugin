﻿using System.Collections.Generic;
using System.Linq;
using Sandbox.Common.ObjectBuilders;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.ModAPI;
using VRage;
using VRage.Game;
using VRage.ObjectBuilders;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class BlockPlacer
    {
        private MyObjectBuilder_CubeBlock CubeBlockBuilderByBlockType(string blockType)
        {
            var definitionBase = MyDefinitionManager.Static
                    .GetAllDefinitions()
                    .First(definition => definition.Id.SubtypeName == blockType);

            var obj = (MyObjectBuilder_CubeBlock) MyObjectBuilderSerializer.CreateNewObject(definitionBase.Id);
            obj.Min = new SerializableVector3I(0, 0, 0);
            obj.SubtypeName = blockType;
            obj.BlockOrientation = new SerializableBlockOrientation(Base6Directions.Direction.Forward,
                Base6Directions.Direction.Up);

            return obj;
        }

        private MyCubeGrid PlaceBlock(MyObjectBuilder_CubeBlock block, Vector3D position, Vector3D forward,
            Vector3D up)
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
            var entity = MyAPIGateway.Entities.CreateFromObjectBuilderAndAdd(gridBuilder);
            MyAPIGateway.Multiplayer.SendEntitiesCreated(new List<MyObjectBuilder_EntityBase> {gridBuilder});

            // Return the created entity
            return (MyCubeGrid) entity;
        }

        public void PlaceBlock(string blockType, Vector3 position, Vector3 orientationForward, Vector3 orientationUp)
        {
            PlaceBlock(CubeBlockBuilderByBlockType(blockType), position, orientationForward, orientationUp);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Definitions;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using VRage.Game.Entity.UseObject;

namespace Iv4xr.SePlugin.Control
{
    public class BlockDefinitionEntityBuilder
    {
        public BlockDefinition CreateAndFill(MyCubeBlockDefinition sourceBlock)
        {
            var type = sourceBlock.GetType().Name;
            var block = CreateBlockDefinition(type);
            AddStandardFields(sourceBlock, block);
            BlockDefinitionCustomFieldsMapper.AddCustomFields(sourceBlock, block);
            return block;
        }

        private BlockDefinition CreateBlockDefinition(string id)
        {
            var fixedId = id.Replace("My", "");
            var type = GetBlockType(BlockDefinitionMapper.Mapping.GetValueOrDefault(fixedId, fixedId));
            var instance = (BlockDefinition)Activator.CreateInstance(type);
            return instance;
        }

        public static Type GetBlockType(string id)
        {
            return typeof(BlockDefinition).Assembly.GetTypes()
                    .FirstOrDefault(type => type.Name == id) ?? typeof(BlockDefinition);
        }

        private void AddStandardFields(MyCubeBlockDefinition myBlockDefinition, BlockDefinition blockDefinition)
        {
            blockDefinition.DefinitionId = myBlockDefinition.ToDefinitionId();
            blockDefinition.CubeSize = myBlockDefinition.CubeSize.ToString();
            blockDefinition.BuildProgressModels =
                    myBlockDefinition.BuildProgressModels.Select(GetBuildProgressModel).ToList();
            blockDefinition.Size = myBlockDefinition.Size.ToPlain();
            blockDefinition.Public = myBlockDefinition.Public;
            blockDefinition.AvailableInSurvival = myBlockDefinition.AvailableInSurvival;
            blockDefinition.Enabled = myBlockDefinition.Enabled;
            blockDefinition.Type = myBlockDefinition.GetType().Name;
            blockDefinition.MountPoints = myBlockDefinition.MountPoints.Select(mp => new MountPoint()
            {
                End = mp.End.ToPlainF(),
                Start = mp.Start.ToPlainF(),
                Normal = mp.Normal.ToPlain(),
                Default = mp.Default,
                Enabled = mp.Enabled,
                ExclusionMask = mp.ExclusionMask,
                PropertiesMask = mp.PropertiesMask,
                PressurizedWhenOpen = mp.PressurizedWhenOpen
            }).ToList();
        }

        public static BuildProgressModel GetBuildProgressModel(MyCubeBlockDefinition.BuildProgressModel bpm)
        {
            return new BuildProgressModel()
            {
                BuildRatioUpperBound = bpm.BuildRatioUpperBound
            };
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Definitions;
using VRage.Game;

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

        private BlockDefinition AddStandardFields(MyCubeBlockDefinition myBlockDefinition,
            BlockDefinition blockDefinition)
        {
            AddBaseFields(myBlockDefinition, blockDefinition);
            blockDefinition.CubeSize = myBlockDefinition.CubeSize.ToString();
            blockDefinition.BuildProgressModels =
                    myBlockDefinition.BuildProgressModels.Select(GetBuildProgressModel).ToList();
            blockDefinition.Size = myBlockDefinition.Size.ToPlain();
            blockDefinition.Public = myBlockDefinition.Public;
            blockDefinition.AvailableInSurvival = myBlockDefinition.AvailableInSurvival;
            blockDefinition.Enabled = myBlockDefinition.Enabled;
            blockDefinition.Type = myBlockDefinition.GetType().Name;
            blockDefinition.Mass = myBlockDefinition.Mass;
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
            blockDefinition.Components = myBlockDefinition.Components.Select(GetComponent).ToList();
            return blockDefinition;
        }

        private static T AddBaseFields<T>(MyDefinitionBase myDefinitionBase, T definitionBase) where T : DefinitionBase
        {
            definitionBase.DefinitionId = myDefinitionBase.ToDefinitionId();
            definitionBase.Public = myDefinitionBase.Public;
            definitionBase.AvailableInSurvival = myDefinitionBase.AvailableInSurvival;
            definitionBase.Enabled = myDefinitionBase.Enabled;
            return definitionBase;
        }

        private static Component GetComponent(MyCubeBlockDefinition.Component myComponent)
        {
            return new Component()
            {
                Count = myComponent.Count,
                DeconstructItem = GetPhysicalItemDefinition(myComponent.DeconstructItem),
                Definition = GetPhysicalItemDefinition(myComponent.Definition),
            };
        }

        private static PhysicalItemDefinition GetPhysicalItemDefinition(MyPhysicalItemDefinition myDefinitionBase)
        {
            var result = new PhysicalItemDefinition();
            AddBaseFields(myDefinitionBase, result);
            result.Health = myDefinitionBase.Health;
            result.Mass = myDefinitionBase.Mass;
            result.Volume = myDefinitionBase.Volume;
            result.Size = myDefinitionBase.Size.ToPlain();
            return result;
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

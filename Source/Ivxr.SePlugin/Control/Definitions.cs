using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Common.ObjectBuilders;
using Sandbox.Definitions;
using VRage.Game;

namespace Iv4xr.SePlugin.Control
{
    public class Definitions : IDefinitions
    {
        private readonly BlockDefinitionEntityBuilder m_builder = new BlockDefinitionEntityBuilder();

        public List<BlockDefinition> BlockDefinitions()
        {
            return MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .Select(m_builder.CreateAndFill)
                    .ToList();
        }

        public List<DefinitionBase> AllDefinitions()
        {
            return MyDefinitionManager.Static
                    .GetAllDefinitions().Select(
                        blockDefinition => new DefinitionBase()
                        {
                            Id = blockDefinition.Id.TypeId.ToString(),
                            BlockType = blockDefinition.Id.SubtypeId.String,
                            Public = blockDefinition.Public,
                            AvailableInSurvival = blockDefinition.AvailableInSurvival,
                            Enabled = blockDefinition.Enabled,
                        }).ToList();
        }

        public Dictionary<string, string> BlockHierarchy()
        {
            return MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .Select(b => b.Id.TypeId.ToString())
                    .Distinct()
                    .SelectMany(GetParentsAsDict)
                    .GroupBy(d => d.Key, d => d.Value)
                    .ToDictionary(d => d.Key, d => d.First()).FromTypeToString();
        }

        public Dictionary<string, string> BlockDefinitionHierarchy()
        {
            return MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .Distinct()
                    .SelectMany(GetParentsAsDict)
                    .GroupBy(d => d.Key, d => d.Value)
                    .ToDictionary(d => d.Key, d => d.First()).FromTypeToString();
        }

        private static Type GetBlockType(string id)
        {
            return typeof(MyObjectBuilder_CubeBlock).Assembly.GetTypes()
                    .FirstOrDefault(type => type.Name == id) ?? typeof(MyObjectBuilder_Thrust).Assembly.GetTypes()
                    .FirstOrDefault(type => type.Name == id);
        }

        private List<Type> GetParentsByType(Type builderType)
        {
            var parents = new List<Type> { builderType };
            var type = builderType.BaseType;
            while (type != null)
            {
                parents.Add(type);
                type = type.BaseType;
            }

            return parents;
        }

        private List<Type> GetParentsByTypeName(string id)
        {
            var builderType = GetBlockType(id);
            return GetParentsByType(builderType).ToList();
        }

        private Dictionary<Type, Type> GetParentsAsDict(string id)
        {
            return GetParentsAsDict(GetParentsByTypeName(id));
        }

        private Dictionary<Type, Type> GetParentsAsDict(MyCubeBlockDefinition id)
        {
            return GetParentsAsDict(GetParentsByType(id.GetType()));
        }

        private Dictionary<Type, Type> GetParentsAsDict(List<Type> parents)
        {
            var result = new Dictionary<Type, Type>();
            for (var i = 0; i < parents.Count - 1; i++)
            {
                result[parents[i]] = parents[i + 1];
            }

            return result;
        }
    }
}

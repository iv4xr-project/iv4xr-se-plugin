using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
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
                            DefinitionId = blockDefinition.ToDefinitionId(),
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
            // Searching through all assemblies was slow, this is some pretty random selection
            // that likely contains the block type we are looking for.
            return typeof(MyObjectBuilder_CubeBlock).Assembly.GetTypes()
                           .FirstOrDefault(type => type.Name == id) ??
                   typeof(MyObjectBuilder_Thrust).Assembly.GetTypes()
                           .FirstOrDefault(type => type.Name == id) ??
                   typeof(MyObjectBuilder_ThrustDefinition).Assembly.GetTypes()
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

        public static void CheckDefinitionIdExistsAndEnabled(MyDefinitionId id)
        {
            var blockDefinition = MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .FirstOrDefault(bd => bd.Id == id);
            if (blockDefinition == null)
            {
                var foundIds = string.Join(",", FindSimilar(id));
                throw new InvalidOperationException($"Invalid definition id {id}, maybe you meant: {foundIds}");
            }
            if (!blockDefinition.Enabled)
            {
                throw new InvalidOperationException($"The block {id} is disabled, maybe dlc?");
            }
        }

        private static IEnumerable<MyDefinitionId> FindSimilar(MyDefinitionId id)
        {
            return MyDefinitionManager.Static.GetAllDefinitions().Select(def => def.Id).Where(
                def => def.TypeId.ToString() == id.TypeId.ToString() ||
                       def.SubtypeId.String == id.SubtypeId.String);
        }
    }
}

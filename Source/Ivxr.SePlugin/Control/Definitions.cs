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
        public List<BlockDefinition> BlockDefinitions()
        {
            return MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .Select(EntityBuilder.GetBuildSeBlockDefinition)
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
                    .ToDictionary(d => d.Key, d => d.First());
        }
        
        public static Type GetBlockType(string id)
        {
            return typeof(MyObjectBuilder_CubeBlock).Assembly.GetTypes()
                    .FirstOrDefault(type => type.Name == id) ?? typeof(MyObjectBuilder_Thrust).Assembly.GetTypes()
                    .FirstOrDefault(type => type.Name == id);
        }

        private List<string> GetBlockParents(string id)
        {
            var parents = new List<string> { id };
            var builderType = GetBlockType(id);
            if (builderType == null) return parents;
            var type = builderType.BaseType;
            while (type != null)
            {
                parents.Add(type.Name);
                type = type.BaseType;
            }
            return parents;
        }

        private Dictionary<string, string> GetParentsAsDict(string id)
        {
            return GetParentsAsDict(GetBlockParents(id));
        }

        private Dictionary<string, string> GetParentsAsDict(List<string> parents)
        {
            var result = new Dictionary<string, string>();
            for (var i = 0; i < parents.Count - 1; i++)
            {
                result[parents[i]] = parents[i + 1];
            }
            return result;
        }

    }
}

using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public class Definitions : IDefinitions
    {
        public List<BlockDefinition> BlockDefinitions()
        {
            return MyDefinitionManager.Static
                    .GetDefinitionsOfType<MyCubeBlockDefinition>()
                    .Select(SeEntityBuilder.GetBuildSeBlockDefinition)
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
    }
}

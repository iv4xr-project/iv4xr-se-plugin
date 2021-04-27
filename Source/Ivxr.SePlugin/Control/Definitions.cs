using System.Collections.Generic;
using System.Linq;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public interface IDefinitions
    {
        List<SeBlockDefinition> BlockDefinitions();
        List<DefinitionBase> AllDefinitions();
    }

    public class Definitions : IDefinitions
    {
        public List<SeBlockDefinition> BlockDefinitions()
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

using System.Collections.Generic;
using System.Linq;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Definitions;

namespace Iv4xr.SePlugin.Control
{
    public interface IDefinitions
    {
        List<SeBlockDefinition> BlockDefinitions();
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
    }
}
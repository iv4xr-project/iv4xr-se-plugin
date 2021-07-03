using System.Collections.Generic;
using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.PluginLib.Control
{
    public interface IDefinitions
    {
        List<BlockDefinition> BlockDefinitions();
        List<DefinitionBase> AllDefinitions();
    }
}

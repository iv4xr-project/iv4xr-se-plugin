using System.Collections.Generic;
using Iv4xr.SePlugin.WorldModel;

namespace Iv4xr.SePlugin.Control
{
    public interface IDefinitions
    {
        List<BlockDefinition> BlockDefinitions();
        List<DefinitionBase> AllDefinitions();
    }
}

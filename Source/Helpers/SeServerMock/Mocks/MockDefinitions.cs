using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockDefinitions : IDefinitions
    {
        public ILog Log { get; set; }


        public List<BlockDefinition> BlockDefinitions()
        {
            return new List<BlockDefinition>();
        }

        public List<DefinitionBase> AllDefinitions()
        {
            return new List<DefinitionBase>();
        }
    }
}

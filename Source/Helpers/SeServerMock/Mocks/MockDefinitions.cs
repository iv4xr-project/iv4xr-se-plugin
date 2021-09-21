using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;

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

        public Dictionary<string, string> BlockHierarchy()
        {
            return new Dictionary<string, string>();
        }
    }
}

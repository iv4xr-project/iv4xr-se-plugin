using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;

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

        public Dictionary<string, string> BlockDefinitionHierarchy()
        {
            return new Dictionary<string, string>();
        }
    }
}

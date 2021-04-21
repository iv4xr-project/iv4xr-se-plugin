using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockDefinitions : IDefinitions
    {
        public ILog Log { get; set; }


        public List<SeBlockDefinition> BlockDefinitions()
        {
            return new List<SeBlockDefinition>();
        }
    }
}
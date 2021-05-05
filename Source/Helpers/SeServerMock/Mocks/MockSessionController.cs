using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Session;
using System;
using System.Collections.Generic;
using System.Text;

namespace SeServerMock.Mocks
{
    class MockSessionController : ISessionController
    {
        public ILog Log { get; set; }

        public void LoadScenario(string scenarioPath)
        {
            Log.WriteLine($"{nameof(MockSessionController)}: *Not* loading scenario: {scenarioPath}");
        }
    }
}

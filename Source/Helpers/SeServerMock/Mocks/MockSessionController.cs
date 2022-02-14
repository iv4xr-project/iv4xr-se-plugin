using System;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;

namespace SeServerMock.Mocks
{
    class MockSessionController : ISessionController
    {
        public ILog Log { get; set; }

        public void LoadScenario(string scenarioPath)
        {
            Log.WriteLine($"{nameof(MockSessionController)}: *Not* loading scenario: {scenarioPath}");
        }

        public void Connect(string address)
        {
            throw new NotImplementedException();
        }

        public void Disconnect()
        {
            throw new NotImplementedException();
        }

        public void ExitGame()
        {
            throw new NotImplementedException();
        }
    }
}

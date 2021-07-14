using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;

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

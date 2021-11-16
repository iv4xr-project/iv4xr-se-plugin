using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.World;

namespace Iv4xr.SePlugin.Control
{
    public class SessionController : ISessionController
    {
        public ILog Log { get; set; }

        public void LoadScenario(string scenarioPath)
        {
            Log.WriteLine($"Loading scenario: '{scenarioPath}'");
            MySessionLoader.LoadSingleplayerSession(scenarioPath);
        }
    }
}

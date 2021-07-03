using Iv4xr.PluginLib;
using Sandbox.Game.World;
using System;
using System.Collections.Generic;
using System.Text;

namespace Iv4xr.SePlugin.Session
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

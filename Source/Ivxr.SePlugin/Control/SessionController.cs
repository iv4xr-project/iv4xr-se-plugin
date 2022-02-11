using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Engine.Networking;
using Sandbox.Game.Gui;
using Sandbox.Game.World;
using Sandbox.Graphics.GUI;
using VRage.GameServices;

namespace Iv4xr.SePlugin.Control
{
    public class SessionController : ISessionController
    {
        public ILog Log { get; set; }

        public void LoadScenario(string scenarioPath)
        {
            Log.WriteLine($"Loading scenario: '{scenarioPath}'");
            //MySessionLoader.UnloadAndExitToMenu();
            MySessionLoader.LoadSingleplayerSession(scenarioPath);
        }

        public void ExitGame()
        {
            MySessionLoader.ExitGame();
        }

        public void Connect(string address)
        {
            MySessionLoader.UnloadAndExitToMenu();
            MyGameService.OnPingServerResponded -= ServerResponded;
            MyGameService.OnPingServerFailedToRespond -= ServerFailedToRespond;
            MyGameService.OnPingServerResponded += ServerResponded;
            MyGameService.OnPingServerFailedToRespond += ServerFailedToRespond;
            MyGameService.PingServer(address);
        }

        public void Disconnect()
        {
            MySessionLoader.UnloadAndExitToMenu();
        }
        
        void ServerResponded(object sender, MyGameServerItem serverItem)
        {
            //CloseHandlers();
            //m_progressScreen.CloseScreen();
            MyLocalCache.SaveLastSessionInfo(null, true, false, serverItem.Name, serverItem.ConnectionString);
            MyJoinGameHelper.JoinGame(serverItem);
        }

        void ServerFailedToRespond(object sender, object e)
        {
            //CloseHandlers();
            //m_progressScreen.CloseScreen();
            MyGuiSandbox.Show(MyCommonTexts.MultiplaterJoin_ServerIsNotResponding);
        }
    }
}

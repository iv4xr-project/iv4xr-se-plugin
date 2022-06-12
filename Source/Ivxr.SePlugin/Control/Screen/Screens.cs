using System;
using System.Threading;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Control.Screen.Terminal;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;
using VRage;
using static Iv4xr.SePlugin.Communication.CallTarget;

namespace Iv4xr.SePlugin.Control.Screen
{
    public abstract class AbstractScreen<TScreen, TData>
            where TScreen : MyGuiScreenBase
            where TData : class
    {
        protected enum ScreenCloseType
        {
            None,
            Now,
            Normal,
        }

        protected TScreen Screen => MyGuiScreenExtensions.EnsureFocusedScreen<TScreen>();

        protected void CheckScreen()
        {
            MyGuiScreenExtensions.EnsureFocusedScreen<TScreen>();
        }

        private readonly ScreenCloseType m_screenClose;

        protected AbstractScreen(ScreenCloseType screenCloseType = ScreenCloseType.None)
        {
            m_screenClose = screenCloseType;
        }

        public virtual TData Data()
        {
            throw new NotImplementedException($"Data not implemented for screen {Screen.DisplayName()}");
        }

        public void Close()
        {
            switch (m_screenClose)
            {
                case ScreenCloseType.Now:
                    Screen.CloseScreenNow();
                    break;
                case ScreenCloseType.Normal:
                    Screen.CloseScreen();
                    break;
                default:
                    throw new InvalidOperationException($"Unable to close window {Screen.DisplayName()}");
            }
        }
    }

    public class Screens : IScreens
    {
        private readonly TerminalScreen m_terminalScreen;
        private readonly MedicalsScreen m_medicalsScreen = new MedicalsScreen();
        private readonly MainMenuScreen m_mainMenuScreen = new MainMenuScreen();
        private readonly MessageBoxScreen m_messageBoxScreen = new MessageBoxScreen();
        private readonly JoinGameScreen m_joinGameScreen = new JoinGameScreen();
        private readonly ServerConnectScreen m_serverConnectScreen = new ServerConnectScreen();
        private readonly NewGameScreen m_newGameScreen = new NewGameScreen();
        private readonly LoadGameScreen m_loadGameScreen = new LoadGameScreen();
        private readonly GamePlayScreen m_gamePlayScreen = new GamePlayScreen();
        private readonly SaveAsScreen m_saveAsScreen = new SaveAsScreen();

        public Screens()
        {
            m_terminalScreen = new TerminalScreen();
        }

        public IMedicals Medicals => m_medicalsScreen;
        public ITerminal Terminal => m_terminalScreen;
        public IMainMenu MainMenu => m_mainMenuScreen;
        public IMessageBox MessageBox => m_messageBoxScreen;
        public IJoinGame JoinGame => m_joinGameScreen;
        public IServerConnect ServerConnect => m_serverConnectScreen;
        public ILoadGame LoadGame => m_loadGameScreen;
        public INewGame NewGame => m_newGameScreen;
        public IGamePlay GamePlay => m_gamePlayScreen;
        public ISaveAs SaveAs => m_saveAsScreen;

        [CallOn(CurrentThread)]
        public string FocusedScreen()
        {
            return MyScreenManager.GetScreenWithFocus().DisplayName();
        }

        [CallOn(CurrentThread)]
        public void WaitUntilTheGameLoaded()
        {
            const int timeoutMs = 60_000;
            const int singleSleepMs = 100;
            const int sleepAfter = 2000;
            for (var i = 0; i < timeoutMs / singleSleepMs; i++)
            {
                Thread.Sleep(singleSleepMs);
                if (!(MyScreenManager.GetScreenWithFocus() is MyGuiScreenLoading) && MyVRage.Platform.SessionReady)
                {
                    Thread.Sleep(sleepAfter);
                    return;
                }
            }

            throw new TimeoutException();
        }
    }
}

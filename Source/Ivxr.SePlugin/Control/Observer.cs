using System.Collections.Generic;
using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Navigation;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox;
using Sandbox.Game.Gui;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage.FileSystem;

namespace Iv4xr.SePlugin.Control
{
    internal class Observer : IObserver
    {
        public ILog Log { get; set; }

        public double Radius
        {
            get => m_lowLevelObserver.Radius;
            set => m_lowLevelObserver.Radius = value;
        }

        private readonly LowLevelObserver m_lowLevelObserver;

        public Observer(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }

        public CharacterObservation Observe()
        {
            return m_lowLevelObserver.GetCharacterObservation();
        }

        public Observation ObserveBlocks()
        {
            return m_lowLevelObserver.GetBlocks();
        }

        public Observation ObserveNewBlocks()
        {
            return m_lowLevelObserver.GetNewBlocks();
        }

        public List<CharacterObservation> ObserveCharacters()
        {
            return m_lowLevelObserver.ObserveCharacters();
        }

        public List<FloatingObject> ObserveFloatingObjects()
        {
            return m_lowLevelObserver.ObserveFloatingObjects();
        }

        public NavGraph NavigationGraph()
        {
            var navGraphEditor = new NavGraphEditor(m_lowLevelObserver);

            return navGraphEditor.GetGraph().ToNavGraph();
        }

        public void SwitchCamera()
        {
            MyGuiScreenGamePlay.Static.SwitchCamera();
        }

        public void TakeScreenshot(string absolutePath)
        {
            // Stolen from se/Sources/TestingToolPlugin/MyTestingToolPlugin.cs
            MyAsyncSaving.Start(null, Path.Combine(MyFileSystem.SavesPath, "..", "iv4XRtempsave"));
            MyGuiSandbox.TakeScreenshot(
                MySandboxGame.ScreenSize.X, MySandboxGame.ScreenSize.Y,
                absolutePath, true, false
            );
        }
    }
}

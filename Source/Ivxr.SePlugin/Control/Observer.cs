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
using VRageMath;

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

        public Entity ObserveControlledEntity()
        {
            return m_lowLevelObserver.GetEntityObservation();
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

        public NavGraph NavigationGraph(string gridId)
        {
            var navGraphEditor = new NavGraphEditor(m_lowLevelObserver);

            return navGraphEditor.GetGraph(gridId).ToNavGraph();
        }

        public void SwitchCamera()
        {
            MyGuiScreenGamePlay.Static.SwitchCamera();
        }

        public void TakeScreenshot(string absolutePath)
        {
            VRageRender.MyRenderProxy.TakeScreenshot(new Vector2(MySandboxGame.Config.ScreenshotSizeMultiplier),
                absolutePath, false, false, true);
        }
    }
}

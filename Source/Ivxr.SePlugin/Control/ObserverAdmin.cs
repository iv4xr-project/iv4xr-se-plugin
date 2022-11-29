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
    internal class ObserverAdmin : IObserverAdmin
    {
        public ILog Log { get; set; }


        private readonly LowLevelObserver m_lowLevelObserver;

        public ObserverAdmin(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }


        public List<CharacterObservation> ObserveCharacters()
        {
            return m_lowLevelObserver.AllCharacters();
        }

        public CubeGrid GridById(string gridId)
        {
            return m_lowLevelObserver.GetCubeGridById(gridId);
        }

        public Block BlockById(string blockId)
        {
            return m_lowLevelObserver.GetBlockDtoById(blockId);
        }
    }
}

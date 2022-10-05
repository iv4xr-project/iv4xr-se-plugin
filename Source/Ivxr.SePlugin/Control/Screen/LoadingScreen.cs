using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class LoadingScreen : AbstractScreen<MyGuiScreenLoading, LoadingData>, ILoading
    {
        public LoadingScreen()
        {
        }

        public override LoadingData Data()
        {
            return LoadScreenData(new LoadingData()
            {
                ScreenToLoad = Screen.GetInstanceFieldOrThrow<MyGuiScreenBase>("m_screenToLoad").DisplayName(),
                ScreenToUnload = Screen.GetInstanceFieldOrThrow<MyGuiScreenBase>("m_screenToUnload").DisplayName(),
                CurrentText = Screen.GetInstanceFieldOrThrow<object>("m_currentText").ToString()
            });
        }
    }
}

using Iv4xr.PluginLib;
using Sandbox.Game;
using Sandbox.Game.Gui;
using Sandbox.Graphics.GUI;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control.Screen.Terminal
{
    public class AbstractTerminalTab<TData> : AbstractScreen<MyGuiScreenTerminal, TData>
            where TData : class

    {
        private readonly MyTerminalPageEnum m_tabIndex;
        private readonly string m_controllerFieldName;

        protected AbstractTerminalTab(MyTerminalPageEnum tabIndex, string controllerFieldName)
        {
            m_tabIndex = tabIndex;
            m_controllerFieldName = controllerFieldName;
        }

        public MyGuiControlTabPage Tab => TabAt((int)m_tabIndex);

        private MyGuiControlTabPage TabAt(int index)
        {
            return Screen.GetTabs().Pages[index];
        }
        
        public object UntypedController => Screen.GetInstanceFieldOrThrow<object>(m_controllerFieldName);
    }
}

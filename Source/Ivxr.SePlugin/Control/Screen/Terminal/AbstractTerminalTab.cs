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

        protected AbstractTerminalTab(MyTerminalPageEnum tabIndex)
        {
            m_tabIndex = tabIndex;
        }

        public MyGuiControlTabPage Tab => TabAt((int)m_tabIndex);

        private MyGuiControlTabPage TabAt(int index)
        {
            return Screen.GetTabs().Pages[index];
        }
    }
}

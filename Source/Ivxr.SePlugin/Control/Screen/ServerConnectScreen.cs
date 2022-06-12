using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Screens;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class ServerConnectScreen : AbstractScreen<MyGuiScreenServerConnect, ServerConnectData>, IServerConnect
    {
        public override ServerConnectData Data()
        {
            return new ServerConnectData()
            {
                Address = Screen.Controls.TextBox("m_addrTextbox").Text,
                AddServerToFavorites = Screen.CheckBox("m_favoriteCheckbox").IsChecked
            };
        }

        public void ToggleAddServerToFavorites()
        {
            Screen.ClickCheckBox("m_favoriteCheckbox");
        }

        public void Connect()
        {
            Screen.Controls.ButtonByText(MyCommonTexts.MultiplayerJoinConnect).PressButton();
        }

        public void EnterAddress(string address)
        {
            Screen.EnterText("m_addrTextbox", address);
        }
    }
}
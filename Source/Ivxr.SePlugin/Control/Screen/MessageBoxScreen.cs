using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class MessageBoxScreen : AbstractScreen<MyGuiScreenMessageBox, MessageBoxData>, IMessageBox
    {
        public void PressYes()
        {
            Screen.ClickButton("m_yesButton");
        }

        public void PressNo()
        {
            Screen.ClickButton("m_noButton");
        }

        public override MessageBoxData Data()
        {
            var screen = Screen;
            return new MessageBoxData()
            {
                Text = screen.MessageText.ToString(),
                Caption = screen.GetInstanceFieldOrThrow<StringBuilder>("m_messageCaption").ToString(),
                ButtonType = (int)screen.GetInstanceFieldOrThrow<MyMessageBoxButtonsType>("m_buttonType")
            };
        }
    }
}
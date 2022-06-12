using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Game.Screens;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class SaveAsScreen : AbstractScreen<MyGuiScreenSaveAs, SaveAsData>, ISaveAs
    {
        public override SaveAsData Data()
        {
            return new SaveAsData()
            {
                Name = Screen.TextBox("m_nameTextbox").Text
            };
        }
        
        public void PressOk()
        {
            Screen.ClickButton("m_okButton");
        }

        public void PressCancel()
        {
            Screen.ClickButton("m_cancelButton");
        }

        public void SetName(string name)
        {
            Screen.TextBox("m_nameTextbox").Text = name;
        }
    }
}
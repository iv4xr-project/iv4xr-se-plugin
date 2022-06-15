using Iv4xr.PluginLib;
using Iv4xr.SpaceEngineers;
using Sandbox.Graphics.GUI;
using SpaceEngineers.Game.GUI;
using VRage.Utils;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class MainMenuScreen : AbstractScreen<MyGuiScreenMainMenu, object>, IMainMenu
    {
        private MyGuiControlElementGroup Buttons()
        {
            return Screen
                    .GetInstanceFieldOrThrow<MyGuiControlElementGroup>("m_elementGroup");
        }
        
        private void PressButtonByText(MyStringId stringId)
        {
            Buttons().ButtonByText(stringId).PressButton();
        }

        public void Continue()
        {
            Screen.ClickButton("m_continueButton");
        }

        public void NewGame()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonCampaign);
        }

        public void LoadGame()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonLoadGame);
        }

        public void JoinGame()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonJoinGame);
        }

        public void Options()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonOptions);
        }

        public void Character()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonInventory);
        }

        public void ExitToWindows()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonExitToWindows);
        }

        public void ExitToMainMenu()
        {
            IvxrPlugin.Context.Reset();
            CheckScreen();
            PressButtonByText(MyCommonTexts.ScreenMenuButtonExitToMainMenu);
        }
        
        public void SaveAs()
        {
            PressButtonByText(MyCommonTexts.LoadScreenButtonSaveAs);
        }

        public void Save()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonSave);
        }
        
        public void Players()
        {
            PressButtonByText(MyCommonTexts.ScreenMenuButtonPlayers);
        }
    }
}

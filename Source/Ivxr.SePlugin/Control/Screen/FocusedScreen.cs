using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel.Screen;
using Sandbox.Graphics.GUI;

namespace Iv4xr.SePlugin.Control.Screen
{
    public class FocusedScreen : AbstractScreen<MyGuiScreenBase, BaseScreenData>, IFocusedScreen
    {
        public FocusedScreen()
        {
        }

        public override BaseScreenData Data()
        {
            return Screen.LoadScreenData(new BaseScreenData());
        }

        public void CloseScreenNow()
        {
            Screen.CloseScreenNow();
        }

        public void CloseScreen()
        {
            Screen.CloseScreen();
        }
    }
}

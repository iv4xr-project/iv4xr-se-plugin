namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public enum MainMenuType
    {
        Main = 0,
        InGame = 1
    }

    public class MainMenuData
    {
        // MainMenu screen is actually used both for main menu and in-game menu.
        public MainMenuType Type;
    }
}

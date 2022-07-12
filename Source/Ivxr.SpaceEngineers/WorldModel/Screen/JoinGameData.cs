using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class JoinGameData
    {
        public int SelectedTab;
        public string SelectedTabName;
        public List<ListedGameInformation> Games;
        public GuiControlBase ServerDetailsButton;
        public GuiControlBase DirectConnectButton;
        public GuiControlBase RefreshButton;
        public GuiControlBase JoinWorldButton;
    }
}

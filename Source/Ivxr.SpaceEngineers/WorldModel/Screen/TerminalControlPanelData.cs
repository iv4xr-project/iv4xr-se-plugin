using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public interface IBlockOrGroupItem
    {
        bool Visible { get; }
        string Text { get; }
    }

    public class BlockItem : IBlockOrGroupItem
    {
        public Block Block;

        public BlockItem(bool visible, string text)
        {
            Visible = visible;
            Text = text;
        }

        public bool Visible { get; }
        public string Text { get; }
    }

    public class BlockGroupItem : IBlockOrGroupItem
    {
        public string Name;
        public List<Block> Blocks;

        public BlockGroupItem(bool visible, string text)
        {
            Visible = visible;
            Text = text;
        }

        public bool Visible { get; }
        public string Text { get; }
    }

    public class TerminalControlPanelData
    {
        public string Search;
        public string NewGroupName;
        public List<IBlockOrGroupItem> GridBlocks;
        public bool ToggleBlock;
        public bool ShowBlockInTerminal;
        public bool ShowBLockInToolbarConfig;
        public bool ShowOnHUD;
        public string Owner;
        public List<string> TransferTo;
        public List<string> ShareBlock;
        public int? ShareBlockSelectedIndex;
    }
}

using System.Text;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities.Blocks;
using VRage.Game.GUI.TextPanel;

namespace Iv4xr.SePlugin.Control.Screen.BlockAdmin
{
    public class TextPanelAdmin : AbstractBlockAdmin<MyTextPanel>, ITextPanelAdmin
    {
        public TextPanelAdmin(IGameSession session, LowLevelObserver observer) : base(session, observer)
        {
        }

        public void SetPublicTitle(string blockId, string publicTitle)
        {
            BlockById(blockId).PublicTitle = new StringBuilder(publicTitle);
        }

        public void SetPrivateTitle(string blockId, string privateTitle)
        {
            BlockById(blockId).PrivateTitle = new StringBuilder(privateTitle);
        }

        public void SetPrivateDescription(string blockId, string privateDescription)
        {
            BlockById(blockId).PrivateDescription = new StringBuilder(privateDescription);
        }

        public void SetContentType(string blockId, int contentType)
        {
            BlockById(blockId).ContentType = (ContentType)contentType;
        }

        public void SetTextPadding(string blockId, float padding)
        {
            BlockById(blockId).PanelComponent.TextPadding = padding;
        }

        public void SetText(string blockId, string text)
        {
            BlockById(blockId).PanelComponent.Text = new StringBuilder(text);
        }

        public void SetAlignment(string blockId, int alignment)
        {
            BlockById(blockId).PanelComponent.Alignment = (TextAlignment) alignment;
        }
        
        public void SetFontSize(string blockId, float fontSize)
        {
            BlockById(blockId).PanelComponent.FontSize = fontSize;
        }
    }
}

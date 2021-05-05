using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockItems : IItems
    {
        public ILog Log { get; set; }
        
        public void Place()
        {
        }

        public void BeginUsingTool()
        {
        }

        public void EndUsingTool()
        {
        }

        public void Equip(ToolbarLocation toolbarLocation)
        {
        }

        public void SetToolbarItem(string name, ToolbarLocation toolbarLocation)
        {
        }

        public Toolbar GetToolbar()
        {
            throw new System.NotImplementedException();
        }
    }
}

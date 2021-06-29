using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace SeServerMock.Mocks
{
    public class MockItems : IItems
    {
        public ILog Log { get; set; }
        
        public void Place()
        {
        }

        public void Remove(string blockId)
        {
            throw new System.NotImplementedException();
        }

        public void SetIntegrity(string blockId, float integrity)
        {
            throw new System.NotImplementedException();
        }

        public void PlaceAt(string blockType, Vector3 position, Vector3 orientationForward, Vector3 orientationUp)
        {
            throw new System.NotImplementedException();
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

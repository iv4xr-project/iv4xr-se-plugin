using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;

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

        public void PlaceAt(string blockType, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp)
        {
            throw new System.NotImplementedException();
        }

        public void Equip(ToolbarLocation toolbarLocation)
        {
        }

        public void SetToolbarItem(string name, ToolbarLocation toolbarLocation)
        {
        }

        public Toolbar Toolbar()
        {
            throw new System.NotImplementedException();
        }
    }
}

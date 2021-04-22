using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;

namespace SeServerMock.Mocks
{
    public class MockItems : IItems
    {
        public ILog Log { get; set; }
        
        public void PlaceBlock()
        {
        }

        public void BeginUseTool()
        {
        }

        public void EndUseTool()
        {
        }

        public void EquipToolbarItem(int slot, int page)
        {
        }

        public void SetToolbarItem(int slot, int page, string itemName)
        {
        }

        public Toolbar GetToolbar()
        {
            throw new System.NotImplementedException();
        }

        public void Interact(InteractionArgs args)
        {
        }
    }
}

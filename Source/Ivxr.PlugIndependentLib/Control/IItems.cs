using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.PluginLib.Control
{
    public interface IItems
    {
        void Place();

        void Remove(string blockId);

        void SetIntegrity(string blockId, float integrity);

        void PlaceAt(string blockType, PlainVec3D position, PlainVec3D orientationForward, PlainVec3D orientationUp);

        void BeginUsingTool();

        void EndUsingTool();

        void Equip(ToolbarLocation toolbarLocation);

        void SetToolbarItem(string name, ToolbarLocation toolbarLocation);

        Toolbar GetToolbar();
    }
}

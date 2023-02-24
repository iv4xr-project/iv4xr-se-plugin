namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class ToolbarItem
    {
        public string Name;
        public bool Enabled;
    }
    
    public class ToolbarItemDefinition: ToolbarItem
    {
        public DefinitionId Id;
    }


    public class ToolbarItemActions : ToolbarItem
    {
        public string ActionId;
    }
    
    public class ToolbarItemTerminalBlock : ToolbarItemActions
    {
        public long BlockEntityId;
    }
}

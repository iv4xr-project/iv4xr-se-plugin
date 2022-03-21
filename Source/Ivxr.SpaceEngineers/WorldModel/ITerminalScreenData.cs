using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class AmountedDefinitionId
    {
        public DefinitionId Id;
        public int Amount;
        
        public override string ToString()
        {
            return $"{Amount}x {Id}";
        }
    }

    public class ProductionQueueItem
    {
        public int Amount;
        public BlueprintDefinition Blueprint;
    }

    public class BlueprintDefinition
    {
        public string DisplayName;
        public List<AmountedDefinitionId> Prerequisites;
        public List<AmountedDefinitionId> Results;
        
        public override string ToString()
        {
            return $"{{{string.Join(" ", Prerequisites)}}}->{{{string.Join(" ", Results)}}}";
        }

    }

    public class TerminalProductionData
    {
        public List<ProductionQueueItem> ProductionQueue;
        public List<AmountedDefinitionId> Inventory;
        public List<BlueprintDefinition> Blueprints;
        public List<string> Assemblers;
        public bool ProductionCooperativeMode;
        public bool ProductionRepeatMode;
    }

    public class TerminalInventoryData
    {
        public List<Inventory> LeftInventories;
        public List<Inventory> RightInventories;
    }
    
    public class TerminalScreenData
    {
        public string SelectedTab;
    }

    public class MessageBoxData
    {
        public string Caption;
        public string Text;
        public int ButtonType;
    }

    public class ServerConnectData
    {
        public string Address;
        public bool AddServerToFavorites;
    }

    public class File
    {
        public string Name;
        public string FullName;
        public bool IsDirectory;
    }

    public class LoadGameData
    {
        public File CurrentDirectory;
        public File RootDirectory;
        public List<File> Files;
    }

    public class GamePlayData
    {
        public List<OreMarker> OreMarkers;
    }
}

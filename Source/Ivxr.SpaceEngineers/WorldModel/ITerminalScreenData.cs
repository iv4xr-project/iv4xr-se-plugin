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
        public List<ProductionQueueItem> Blueprints;
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
        public TerminalProductionData Production;
        public TerminalInventoryData Inventory;
    }
}

using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class TerminalProductionData
    {
        public List<ProductionQueueItem> ProductionQueue;
        public List<AmountedDefinitionId> Inventory;
        public List<BlueprintDefinition> Blueprints;
        public List<string> Assemblers;
        public bool ProductionCooperativeMode;
        public bool ProductionRepeatMode;
    }
}
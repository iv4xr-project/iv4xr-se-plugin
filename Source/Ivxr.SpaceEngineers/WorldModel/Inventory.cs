using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class Inventory
    {
        public float CurrentMass;
        public float CurrentVolume;
        public float MaxMass;
        public float MaxVolume;
        public float CargoPercentage;
        public List<InventoryItem> Items;
    }
}

using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class OreMarker: LocationMarker
    {
        public double Distance;
        public List<DefinitionId> Materials;
    }
    
    public class LocationMarker
    {
        public PlainVec3D Position;
        public string Text;
    }
}

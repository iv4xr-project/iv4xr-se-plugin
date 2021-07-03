using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    // Intended to be maximally compatible with Iv4xr.framework.world.WorldModel from the iv4XR Java framework.
    public class Observation
    {
        public CharacterObservation Character;
        public List<CubeGrid> Grids;
    }
}

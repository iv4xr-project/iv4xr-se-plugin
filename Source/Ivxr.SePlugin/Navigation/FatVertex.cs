using System.Collections.Generic;
using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.SePlugin.Navigation
{
    /// <summary>
    /// Apart from position, stores also edges leading to connected vertices.
    /// </summary>
    public class FatVertex
    {
        public readonly PlainVec3D Position;

        public readonly List<FatVertex> Neighbours = new List<FatVertex>(capacity: 8);

    }
}

using System.Collections.Generic;
using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.SePlugin.Navigation
{
    /// <summary>
    /// Apart from position, stores also edges leading to connected vertices.
    /// </summary>
    public class FatNode
    {
        public readonly PlainVec3D Position;

        public readonly List<FatNode> Neighbours = new List<FatNode>(capacity: 8);

        public FatNode(PlainVec3D position)
        {
            Position = position;
        }
    }
}

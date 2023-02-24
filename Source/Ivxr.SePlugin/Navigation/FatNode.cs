using System.Collections.Generic;
using Iv4xr.SpaceEngineers.Navigation;
using Iv4xr.SpaceEngineers.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Navigation
{
    /// <summary>
    /// Apart from position, stores also edges leading to connected vertices.
    /// 
    /// Use FatNodeBuilder to create instances of this class.
    /// </summary>
    public class FatNode
    {
        public readonly string Id;
        
        public readonly PlainVec3D Position;

        public readonly List<FatNode> Neighbours = new List<FatNode>(capacity: 8);

        public FatNode(string id, PlainVec3D position)
        {
            Id = id;
            Position = position;
        }

        internal Node ToSlimNode()
        {
            return new Node(Id, Position);
        }
    }

}

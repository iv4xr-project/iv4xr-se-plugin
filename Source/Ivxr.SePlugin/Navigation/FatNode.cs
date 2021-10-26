using System.Collections.Generic;
using System.Linq;
using System.Threading;
using Iv4xr.PluginLib.Navigation;
using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.SePlugin.Navigation
{
    /// <summary>
    /// Apart from position, stores also edges leading to connected vertices.
    /// </summary>
    public class FatNode
    {
        public readonly int Id;
        
        public readonly PlainVec3D Position;

        public readonly List<FatNode> Neighbours = new List<FatNode>(capacity: 8);

        private static int m_nextId;

        private static int GenerateId()
        {
            return Interlocked.Increment(ref m_nextId);
        }

        public FatNode(PlainVec3D position)
        {
            Id = GenerateId();
            Position = position;
        }

        internal Node ToSlimNode()
        {
            return new Node(Id, Position);
        }
    }
}

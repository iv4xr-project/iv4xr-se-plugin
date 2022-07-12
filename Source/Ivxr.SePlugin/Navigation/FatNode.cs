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
        public readonly int Id;
        
        public readonly PlainVec3D Position;

        public readonly List<FatNode> Neighbours = new List<FatNode>(capacity: 8);

        public FatNode(int id, PlainVec3D position)
        {
            Id = id;
            Position = position;
        }

        internal Node ToSlimNode()
        {
            return new Node(Id, Position);
        }
    }

    public class FatNodeBuilder
    {
        private int m_nextId;

        private int GenerateId()
        {
            // Multi-threaded support not necessary for now (Interlocked.Increment(ref m_nextId))
            return m_nextId++;
        }

        private FatNode Create(PlainVec3D position)
        {
            return new FatNode(GenerateId(), position);
        }

        /// <summary>
        /// Use in a single thread only.
        /// </summary>
        public FatNode Create(Vector3D position)
        {
            return Create(position.ToPlain());
        }
    }
}

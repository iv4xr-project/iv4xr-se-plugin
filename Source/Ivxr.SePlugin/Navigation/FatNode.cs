using System.Collections.Generic;
using System.Linq;
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

        private int m_temporaryIndex;

        public FatNode(PlainVec3D position)
        {
            Position = position;
        }
        
        /// <summary>
        /// Use the FatNavGraph extension method ToNavGraph to call this method from outside.
        /// </summary>
        ///
        /// The method is here, so that it has access to the TemporaryIndex.
        internal static NavGraph ConvertFatNavGraphToSlim(FatNavGraph fatNavGraph)
        {
            var i = 0;
            var vertexPositions = new List<PlainVec3D>(fatNavGraph.Nodes.Count);
            
            foreach (var fatNode in fatNavGraph.Nodes)
            {
                fatNode.m_temporaryIndex = i;
                i++;
                
                vertexPositions.Add(fatNode.Position);
            }
            
            var setOfEdges = new HashSet<Edge>();
            foreach (var fatNode in fatNavGraph.Nodes)
            {
                foreach (var neighbour in fatNode.Neighbours)
                {
                    setOfEdges.Add(new Edge(fatNode.m_temporaryIndex, neighbour.m_temporaryIndex));
                }
            }

            return new NavGraph(vertexPositions, setOfEdges.ToList());
        }
    }
}

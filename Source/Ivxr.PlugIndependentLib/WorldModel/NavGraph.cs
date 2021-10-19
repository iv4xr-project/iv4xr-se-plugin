using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    public class NavGraph
    {
        // Vertices indexed from zero.
        public readonly List<PlainVec3D> VertexPositions;

        public readonly List<Edge> Edges;

        public NavGraph(int vertexCapacity = 0)
        {
            VertexPositions = new List<PlainVec3D>(vertexCapacity);
            Edges = new List<Edge>(2 * vertexCapacity);  // Roughly estimated average edges per node.
        }

        public NavGraph(List<PlainVec3D> vertexPositions, List<Edge> edges)
        {
            VertexPositions = vertexPositions;
            Edges = edges;
        }
    }
}

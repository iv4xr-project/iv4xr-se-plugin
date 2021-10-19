using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    public class NavGraph
    {
        // Vertices indexed from zero.
        public List<PlainVec3D> VertexPositions;

        public List<Edge> Edges;
    }
}

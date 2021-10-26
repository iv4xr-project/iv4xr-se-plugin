using System.Collections.Generic;

namespace Iv4xr.PluginLib.Navigation
{
    public class NavGraph
    {
        public readonly List<Node> Nodes;

        public readonly List<Edge> Edges;

        public NavGraph(List<Node> nodes, List<Edge> edges)
        {
            Nodes = nodes;
            Edges = edges;
        }
    }
}

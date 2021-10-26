using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.SePlugin.Navigation
{
    public class FatNavGraph
    {
        public readonly List<FatNode> Nodes = new List<FatNode>();
    }

    public static class FatNavGraphExtensions
    {
        public static NavGraph ToNavGraph(this FatNavGraph fatNavGraph)
        {
            var setOfEdges = new HashSet<Edge>();
            foreach (var fatNode in fatNavGraph.Nodes)
            {
                foreach (var neighbour in fatNode.Neighbours)
                {
                    setOfEdges.Add(new Edge(fatNode.Id, neighbour.Id));
                }
            }

            return new NavGraph(
                nodes: fatNavGraph.Nodes.Select(n => n.ToSlimNode()).ToList(),
                edges: setOfEdges.ToList());
        }
    }
}

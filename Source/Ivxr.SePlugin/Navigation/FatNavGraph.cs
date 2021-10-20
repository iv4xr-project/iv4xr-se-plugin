using System.Collections.Generic;
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
            return FatNode.ConvertFatNavGraphToSlim(fatNavGraph);
        }
    }
}

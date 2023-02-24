using System;
using System.Linq;

namespace Iv4xr.SpaceEngineers.Navigation
{

    public class Edge
    {
        public readonly string Id;
        
        public readonly string I;
        
        public readonly string J;

        public readonly string Data = "";

        public Edge(string u, string v) : this(
            MakeId(u, v), u, v)
        {
            
        }

        public Edge(string id, string u, string v)
        {
            if (u == v)
                throw new ArgumentException("Edge cannot exist between equal indices.");
            string[] nodeIds = { u, v };
            Array.Sort(nodeIds);
            I = nodeIds.First();
            J = nodeIds.Last();
            Id = id;
            Id = $"E-{I}-{J}";
        }

        private static string MakeId(string u, string v)
        {
            string[] nodeIds = { u, v };
            Array.Sort(nodeIds);
            var i = nodeIds.First();
            var j = nodeIds.Last();
            return $"E-{i}-{j}";
        }

        public override bool Equals(object obj)
        {
            if (!(obj is Edge other))
                return false;

            return (I == other.I) && (J == other.J);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return I.GetHashCode() + J.GetHashCode() * 33013;
            }
        }
    }

}

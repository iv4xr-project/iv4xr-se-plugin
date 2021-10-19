using System;

namespace Iv4xr.PluginLib.WorldModel
{

    public class Edge
    {
        /// <summary>Smaller vertex index.</summary>
        public readonly int I;
        
        /// <summary>Greater vertex index.</summary>
        public readonly int J;

        public Edge(int u, int v)
        {
            if (u == v)
                throw new ArgumentException("Edge cannot exist between equal indices.");
            if (u < 0 || v < 0)
                throw new ArgumentException("Vertex index cannot be negative.");

            I = Math.Min(u, v);
            J = Math.Max(u, v);
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
                return I + J * 33013;
            }
        }
    }

}

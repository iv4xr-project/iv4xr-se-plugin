using Iv4xr.SpaceEngineers.WorldModel;

namespace Iv4xr.SpaceEngineers.Navigation
{
    public class Node
    {
        public readonly string Id;
        public readonly PlainVec3D Position;

        public Node(string id, PlainVec3D position)
        {
            Id = id;
            Position = position;
        }
    }
}

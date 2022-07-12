namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class Entity: Pose
    {
        public string Id;
        public string Name;
        public string DisplayName;
        public PlainVec3D Velocity;
        public bool InScene;
        public DefinitionId DefinitionId;
    }
}

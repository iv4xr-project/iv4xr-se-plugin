using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class BlockDefinition: PhysicalModelDefinition
    {
        public List<BuildProgressModel> BuildProgressModels;
        public string Type;
        public string CubeSize;
        public PlainVec3I Size;
        public List<MountPoint> MountPoints;
        public List<Component> Components;
    }
}

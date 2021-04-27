using System.Collections.Generic;
using VRageMath;

namespace Iv4xr.SePlugin.WorldModel
{
    public class BlockDefinition
    {
        public string Id;
        public string BlockType;
        public List<BuildProgressModel> BuildProgressModels;
        //public string Model;
        public string CubeSize;
        public Vector3I Size;
        public List<MountPoint> MountPoints;
        public bool Public;
        public bool AvailableInSurvival;
        public bool Enabled;
    }
}

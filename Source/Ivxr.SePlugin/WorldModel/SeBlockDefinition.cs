using System.Collections.Generic;
using VRageMath;

namespace Iv4xr.SePlugin.WorldModel
{
    public class SeBlockDefinition
    {
        //public string Id;
        public string BlockType;
        public List<SeBuildProgressModel> BuildProgressModels;
        //public string Model;
        public string CubeSize;
        public Vector3I Size;
    }
}
using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    public class BlockDefinition: DefinitionBase
    {
        public List<BuildProgressModel> BuildProgressModels;
        public string Type;
        public string CubeSize;
        public PlainVec3I Size;
        public List<MountPoint> MountPoints;
    }
}

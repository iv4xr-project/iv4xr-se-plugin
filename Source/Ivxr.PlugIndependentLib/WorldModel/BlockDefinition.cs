using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    public class BlockDefinition
    {
        public DefinitionId DefinitionId;
        public List<BuildProgressModel> BuildProgressModels;
        public string Type;
        public string CubeSize;
        public PlainVec3I Size;
        public List<MountPoint> MountPoints;
        public bool Public;
        public bool AvailableInSurvival;
        public bool Enabled;
    }
}

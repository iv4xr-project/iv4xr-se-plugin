using System.Collections.Generic;

namespace Iv4xr.PluginLib.WorldModel
{
    public class Block : Entity
    {
        //public float GridSize;  // NOTE: pull up to Grid if we add grids.
        public float MaxIntegrity;
        public float BuildIntegrity;
        public float Integrity;

        public DefinitionId DefinitionId;

        public PlainVec3D MinPosition;
        public PlainVec3D MaxPosition;
        public PlainVec3I Size;
        public bool Functional;
        public bool Working;
        public List<UseObject> UseObjects;
    }
}

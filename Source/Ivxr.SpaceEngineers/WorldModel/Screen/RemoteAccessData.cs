using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.WorldModel.Screen
{
    public class RemoteAccessData
    {
        public List<RemoteGridData> Grids;
    }

    public class RemoteGridData
    {
        public string Name;
        public float? Distance;
        public bool IsSelectable;
    }
}

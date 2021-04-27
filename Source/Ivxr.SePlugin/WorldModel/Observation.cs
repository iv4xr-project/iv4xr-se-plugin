﻿using System.Collections.Generic;

namespace Iv4xr.SePlugin.WorldModel
{
    // Intended to be maximally compatible with Iv4xr.framework.world.WorldModel from the iv4XR Java framework.
    public class Observation
    {
        public string AgentID;

        public PlainVec3D Position; // Agent's position
        public PlainVec3D OrientationForward;
        public PlainVec3D OrientationUp;
        public PlainVec3D Velocity; // Agent's velocity
        public PlainVec3D Extent; // Agent's dimensions (x,y,z size/2)

        public List<Entity> Entities;

        public List<CubeGrid> Grids;
    }
}

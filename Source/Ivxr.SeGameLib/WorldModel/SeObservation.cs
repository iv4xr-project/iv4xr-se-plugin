using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace EU.Iv4xr.SeGameLib.WorldModel
{
	// Intended to be maximally compatible with eu.iv4xr.framework.world.WorldModel from the iv4XR Java framework.
	public class SeObservation
	{
		public string AgentId;

		public Vector3D Position;  // Agent's position
		public Vector3D Velocity;  // Agent's velocity
		public Vector3D Extent;  // Agent's dimensions (x,y,z size/2)

		// TODO(PP): Add entities.
	}
}

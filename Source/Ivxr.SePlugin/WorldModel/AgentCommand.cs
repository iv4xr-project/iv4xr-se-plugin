using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace Iv4xr.SePlugin.WorldModel
{
	// to deserialize Java tuple
	public class MoveCommandArgs
	{
		public Vector3D Object1;  // Move vector.
		public bool Object2;  // Jump.

		// Access by the proper name via properties.
		public Vector3 MoveIndicator => Object1;
		public bool Jump => Object2;
	}

	public class AgentCommand<ArgumentType>
	{
		public string Cmd;
		public string AgentId;
		public string TargetId;
		public ArgumentType Arg;
	}
}

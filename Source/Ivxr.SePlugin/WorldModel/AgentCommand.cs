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

	public class MoveAndRotateArgs
	{
		public Vector3D Movement;  // D = double, needed for LitJson deserialization

		public Vector3D Rotation3 // D = double
		{
			get { return m_rotation3; }
			set  // Set also Vector2 Rotation.
			{
				m_rotation3 = value;
				Rotation.X = (float)value.X;
				Rotation.Y = (float)value.Y;
			}
		}
		private Vector3D m_rotation3;

		public double Roll;

		public Vector2 Rotation;
	}

	public class AgentCommand<ArgumentType>
	{
		public string Cmd;
		public string AgentId;
		public string TargetId;
		public ArgumentType Arg;
	}
}

using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace Iv4xr.SeGameLib.WorldModel
{
	/// <summary>
	/// Yet another Vector3D class. This time without any properties for easy (JSON) serialization.
	/// </summary>
	public struct PlainVec3D
	{
		public double X;
		public double Y;
		public double Z;

		public PlainVec3D(double x, double y, double z)
		{
			X = x;
			Y = y;
			Z = z;
		}

		public PlainVec3D(Vector3D vector3D)
		{
			X = vector3D.X;
			Y = vector3D.Y;
			Z = vector3D.Z;
		}
	}
}

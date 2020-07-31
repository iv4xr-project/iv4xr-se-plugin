using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace SeServerMock.Mocks
{
	internal class MockCharacterController : ICharacterController
	{
		public void Move(Vector3 move, Vector2 rotation, float roll)
		{
			// Noop.
		}

		public void Move(MoveAndRotateArgs args)
		{
			// Noop.
		}
	}
}

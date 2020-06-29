using Iv4xr.SePlugin.Control;
using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace SeServerMock.Mocks
{
	internal class MockCharacterController : ICharacterController
	{
		public void Move(Vector3 move)
		{
			// Noop.
		}
	}
}

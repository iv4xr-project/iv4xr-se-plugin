using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
	public interface ICharacterController
	{
		void Move(Vector3 move);
	}

	public class CharacterController : ICharacterController
	{
		private IGameSession m_session;

		public CharacterController(IGameSession session)
		{
			m_session = session;
		}

		public void Move(Vector3 move)
		{
			var entityController = m_session.Character.ControllerInfo.Controller;

			entityController.ControlledEntity.MoveAndRotate(move, Vector2.Zero, 0);
		}
	}
}

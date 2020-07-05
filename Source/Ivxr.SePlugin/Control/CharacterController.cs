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
			if (m_session.Character is null)
				throw new NullReferenceException("I'm out of character!");  // Should not happen.
			
			var entityController = m_session.Character.ControllerInfo.Controller;

			if (entityController is null)  // Happens when the character enters a vehicle, for example.
				throw new NotSupportedException("Entity control not possible now."); 

			entityController.ControlledEntity.MoveAndRotate(move, Vector2.Zero, 0);
		}
	}
}

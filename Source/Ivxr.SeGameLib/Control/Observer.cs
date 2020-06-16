using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using EU.Iv4xr.SeGameLib.WorldModel;
using Iv4xr.SeGameLib.WorldModel;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using VRageMath;

namespace Iv4xr.SeGameLib.Control
{
	public interface IObserver
	{
		SeObservation GetObservation();
	}

	public class Observer : IObserver
	{
		private MyCharacter m_character;

		private readonly PlainVec3D AgentExtent = new PlainVec3D(0.5, 1, 0.5);  // TODO(PP): It's just a quick guess, check the reality.

		public void InitSession()
		{
			var playerCount = Sync.Players.GetOnlinePlayerCount();
			if (playerCount <= 0)
			{
				throw new InvalidOperationException("Didn't find any players.");
			}

			m_character = Sync.Players.GetOnlinePlayers().First().Character;
		}

		public void EndSession()
		{
			m_character = null;
		}

		public SeObservation GetObservation()
		{
			if (m_character is null)
				InitSession();

			return new SeObservation
			{
				AgentID = "se0",
				Position = new PlainVec3D(GetPlayerPosition()),  // Consider reducing allocations.
				Velocity = new PlainVec3D(GetPlayerVelocity()),
				Extent = AgentExtent
			};
		}

		private Vector3D GetPlayerPosition()
		{
			return m_character.PositionComp.GetPosition();
		}

		private Vector3D GetPlayerVelocity()
		{
			// TODO(PP): Calculate velocity!
			return Vector3D.Zero; 
		}
	}
}

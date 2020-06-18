using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using VRage.Game.Entity;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
	public interface IObserver
	{
		SeObservation GetObservation();
	}

	public class Observer : IObserver
	{
		public ILog Log { get; set; }

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
				Extent = AgentExtent,
				Entities = CollectSurroundingEntities()
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

		private List<SeEntity> CollectSurroundingEntities()
		{
			var ivEntities = new List<SeEntity>();

			var characterPosition = GetPlayerPosition();

			var sphere = new BoundingSphereD(characterPosition, radius: 25.0);
			List<MyEntity> entities = MyEntities.GetEntitiesInSphere(ref sphere);

			try
			{
				foreach (MyEntity entity in entities)
				{
					var ivEntity = new SeEntity()
					{
						Id = entity.Name,
						Position = new PlainVec3D(entity.PositionComp.GetPosition())
					};

					ivEntities.Add(ivEntity);

					if (ivEntities.Count() > 100)  // TODO(PP): Define as param.
					{
						Log?.WriteLine($"{nameof(CollectSurroundingEntities)}: Too many entities!");
						break;
					}
				}
			}
			finally
			{
				entities.Clear();
			}

			return ivEntities;
		}
	}
}

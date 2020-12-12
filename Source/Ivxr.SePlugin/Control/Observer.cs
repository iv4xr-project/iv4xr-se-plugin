using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Entities.Cube;
using Sandbox.Game.Multiplayer;
using VRage.Game.Entity;
using VRage.Game.ModAPI;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
	public interface IObserver
	{
		SeObservation GetObservation();
	}

	internal class Observer : IObserver
	{
		public ILog Log { get; set; }

		private readonly PlainVec3D AgentExtent = new PlainVec3D(0.5, 1, 0.5);  // TODO(PP): It's just a quick guess, check the reality.

		private GameSession m_gameSession;
		private MyCharacter Character => m_gameSession.Character;

		public Observer(GameSession session)
		{
			m_gameSession = session;
		}

		public SeObservation GetObservation()
		{
			var characterPosition = GetPlayerPosition();
			var sphere = new BoundingSphereD(characterPosition, radius: 25.0);

			return new SeObservation
			{
				AgentID = "se0",
				Position = new PlainVec3D(characterPosition),  // Consider reducing allocations.
				Velocity = new PlainVec3D(GetPlayerVelocity()),
				Extent = AgentExtent,
				Entities = CollectSurroundingEntities(sphere),  // TODO(PP): Don't return both entities and blocks (duplicate work).
				Blocks = CollectSurroundingBlocks(sphere)
			};
		}

		private Vector3D GetPlayerPosition()
		{
			return Character.PositionComp.GetPosition();
		}

		private Vector3D GetPlayerVelocity()
		{
			// TODO(PP): Calculate velocity!
			return Vector3D.Zero; 
		}

		private IEnumerable<MyEntity> EnumerateSurroundingEntities(BoundingSphereD sphere)
		{
			List<MyEntity> entities = MyEntities.GetEntitiesInSphere(ref sphere);

			try
			{
				foreach (MyEntity entity in entities)
					yield return entity;
			}
			finally
			{
				entities.Clear();
			}
		}

		private List<SeEntity> CollectSurroundingEntities(BoundingSphereD sphere)
		{
			var ivEntities = new List<SeEntity>();

			foreach (MyEntity entity in EnumerateSurroundingEntities(sphere))
			{
				var ivEntity = new SeEntity()
				{
					Id = entity.Name,
					Position = new PlainVec3D(entity.PositionComp.GetPosition())
				};

				ivEntities.Add(ivEntity);

				if (ivEntities.Count() > 1000)  // TODO(PP): Define as param.
				{
					Log?.WriteLine($"{nameof(CollectSurroundingEntities)}: Too many entities!");
					break;
				}
			}

			return ivEntities;
		}

		private List<SeBlock> CollectSurroundingBlocks(BoundingSphereD sphere)
		{
			var ivBlocks = new List<SeBlock>();  // iv4XR interface blocks ("SE blocks" from the outside)

			foreach (MyEntity entity in EnumerateSurroundingEntities(sphere))
			{
				var grid = entity as MyCubeGrid;
				if (grid is null)
					continue;
				
				var foundBlocks = new HashSet<MySlimBlock>();
				grid.GetBlocksInsideSphere(ref sphere, foundBlocks);  // NOTE: This might be slow (profiled ages ago)

				foreach (IMySlimBlock sourceBlock in foundBlocks)
				{
					var ivBlock = new SeBlock
					{
						// TODO(PP): generate some Id?
						Position = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Position)),
						MaxIntegrity = sourceBlock.MaxIntegrity,
						BuildIntegrity = sourceBlock.BuildIntegrity,
						Integrity = sourceBlock.Integrity
					};

					ivBlocks.Add(ivBlock);

					if (ivBlocks.Count() > 1000)  // TODO(PP): Define as param.
					{
						Log?.WriteLine($"{nameof(CollectSurroundingBlocks)}: Too many blocks!");
						break;
					}
				}
			}

			return ivBlocks;
		}
	}
}

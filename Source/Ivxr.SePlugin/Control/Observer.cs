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
		SeObservation GetObservation(ObservationArgs observationArgs);
	}

	internal class Observer : IObserver
	{
		public ILog Log { get; set; }


		private HashSet<int> m_previousBlockIds = new HashSet<int>();
		private readonly LowLevelObserver m_lowLevelObserver;

		public Observer(GameSession session)
		{
			// TODO(PP): dependency injection
			m_lowLevelObserver = new LowLevelObserver(session);
		}

		public SeObservation GetObservation(ObservationArgs observationArgs)
		{
			var mode = ((observationArgs.ObservationMode == ObservationMode.DEFAULT)
				? ObservationMode.BASIC
				: observationArgs.ObservationMode);

			var observation = m_lowLevelObserver.GetBasicObservation();

			if (mode == ObservationMode.BASIC)
			{
				return observation;
			}

			var sphere = new BoundingSphereD(m_lowLevelObserver.GetPlayerPosition(), radius: 25.0);

			switch (mode)
			{
				case ObservationMode.ENTITIES:
					observation.Entities = CollectSurroundingEntities(sphere);
					break;

				case ObservationMode.BLOCKS:
				case ObservationMode.NEW_BLOCKS:
					observation.Blocks = CollectSurroundingBlocks(sphere, mode);
					break;

				default:
					throw new ArgumentOutOfRangeException();
			}

			return observation;
		}

		public SeObservation GetObservation()
		{
			return GetObservation(ObservationArgs.Default);
		}

		private List<SeEntity> CollectSurroundingEntities(BoundingSphereD sphere)
		{
			var ivEntities = new List<SeEntity>();

			foreach (MyEntity entity in m_lowLevelObserver.EnumerateSurroundingEntities(sphere))
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

		private static string GetSeBlockType(IMySlimBlock sourceBlock)
		{ 
			// block.BlockDefinition.Id.TypeId not used, SubtypeName seems sufficiently unique for now.
			return sourceBlock.BlockDefinition?.Id is null
				? "null"
				: sourceBlock.BlockDefinition.Id.SubtypeName;
		}

		private List<SeBlock> CollectSurroundingBlocks(BoundingSphereD sphere, ObservationMode mode)
		{
			SeBlock CreateSeBlock(MyCubeGrid grid, MySlimBlock sourceBlock)
			{
				return new SeBlock
				{
					Position = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Position)),
					MaxIntegrity = sourceBlock.MaxIntegrity,
					BuildIntegrity = sourceBlock.BuildIntegrity,
					Integrity = sourceBlock.Integrity,
					BlockType = GetSeBlockType(sourceBlock),
					MinPosition = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Min)),
					MaxPosition = new PlainVec3D(grid.GridIntegerToWorld(sourceBlock.Max)),

					// Note: it does not have to be the same as block.Min - block.Max (because of rotations)
					Size = new PlainVec3D(sourceBlock.BlockDefinition.Size),

					OrientationForward = new PlainVec3D(grid.WorldMatrix.GetDirectionVector(sourceBlock.Orientation.Forward)),
					OrientationUp      = new PlainVec3D(grid.WorldMatrix.GetDirectionVector(sourceBlock.Orientation.Up))
				};
			}

			var ivBlocks = new List<SeBlock>();  // iv4XR interface blocks ("SE blocks" from the outside)

			var blockIds = new HashSet<int>();

			foreach (MyEntity entity in m_lowLevelObserver.EnumerateSurroundingEntities(sphere))
			{
				var grid = entity as MyCubeGrid;
				if (grid is null)
					continue;
				
				var foundBlocks = new HashSet<MySlimBlock>();
				m_lowLevelObserver.GetBlocksInsideSphere(grid, ref sphere, foundBlocks);  // NOTE: Likely slow.

				foreach (MySlimBlock sourceBlock in foundBlocks)
				{
					int blockId = sourceBlock.UniqueId;
					blockIds.Add(blockId);  // TODO(PP): Consider not updating the hash set when not in NEW_BLOCKS mode.

					if (mode == ObservationMode.NEW_BLOCKS)
					{
						if (m_previousBlockIds.Contains(blockId))
							continue;
					}

					ivBlocks.Add(CreateSeBlock(grid, sourceBlock));

					if (ivBlocks.Count() > 1000)  // TODO(PP): Define as param.
					{
						Log?.WriteLine($"{nameof(CollectSurroundingBlocks)}: Too many blocks!");
						break;
					}
				}
			}

			m_previousBlockIds = blockIds;

			Log?.WriteLine($"{nameof(CollectSurroundingBlocks)}: Found {ivBlocks.Count} new blocks.");

			return ivBlocks;
		}
	}
}

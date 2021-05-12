using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using VRage.Game.Entity;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    internal class LowLevelObserver
    {
        public ILog Log { get; set; }

        private readonly GameSession m_gameSession;

        private readonly PlainVec3D
                m_agentExtent = new PlainVec3D(0.5, 1, 0.5); // TODO(PP): It's just a quick guess, check the reality.

        private readonly SeEntityBuilder m_seEntityBuilder;

        public LowLevelObserver(GameSession gameSession)
        {
            m_gameSession = gameSession;
            m_seEntityBuilder = new SeEntityBuilder() {Log = Log};
        }

        private MyCharacter Character => m_gameSession.Character;

        private Vector3D GetPlayerPosition()
        {
            return Character.PositionComp.GetPosition();
        }

        private Vector3D GetPlayerVelocity()
        {
            // TODO(PP): Calculate velocity!
            return Vector3D.Zero;
        }

        public Observation GetBasicObservation()
        {
            var orientation = Character.PositionComp.GetOrientation();
            return new Observation
            {
                Id = "se0",
                Position = new PlainVec3D(GetPlayerPosition()), // Consider reducing allocations.
                OrientationForward = new PlainVec3D(orientation.Forward),
                OrientationUp = new PlainVec3D(orientation.Up),
                Velocity = new PlainVec3D(GetPlayerVelocity()),
                Extent = m_agentExtent,
            };
        }

        public Observation GetNewBlocks()
        {
            var observation = GetBasicObservation();
            observation.Grids = CollectSurroundingBlocks(GetBoundingSphereD(), ObservationMode.NEW_BLOCKS);
            return observation;
        }
        
        public Observation GetBlocks()
        {
            var observation = GetBasicObservation();
            observation.Grids = CollectSurroundingBlocks(GetBoundingSphereD(), ObservationMode.BLOCKS);
            return observation;
        }

        private BoundingSphereD GetBoundingSphereD()
        {
            return new BoundingSphereD(GetPlayerPosition(), radius: 25.0);
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

        private List<CubeGrid> CollectSurroundingBlocks(BoundingSphereD sphere, ObservationMode mode)
        {
            return EnumerateSurroundingEntities(sphere)
                    .OfType<MyCubeGrid>()
                    .Select(grid => m_seEntityBuilder.CreateSeGrid(grid, sphere, mode)).ToList();
        }
    }
}

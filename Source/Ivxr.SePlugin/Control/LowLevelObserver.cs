using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.Weapons;
using Sandbox.Game.World;
using VRage.Game.Entity;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    internal class LowLevelObserver
    {
        public ILog Log { get; set; }

        public double Radius
        {
            get => m_radius;
            set
            {
                Observer.ValidateRadius(value);
                m_radius = value;
            }
        }

        private double m_radius = Observer.DefaultRadius;

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
            Character.GetNetState(out var client);
            return client.MovementDirection * client.MovementSpeed;
        }

        public Observation GetBasicObservation()
        {
            var orientation = Character.PositionComp.GetOrientation();
            return new Observation
            {
                Id = "se0",
                Position = GetPlayerPosition().ToPlain(), // Consider reducing allocations.
                OrientationForward = orientation.Forward.ToPlain(),
                OrientationUp = orientation.Up.ToPlain(),
                Velocity = GetPlayerVelocity().ToPlain(),
                Extent = PlayerExtent(),
                Camera = new Pose()
                {
                    Position = MySector.MainCamera.Position.ToPlain(),
                    OrientationForward = MySector.MainCamera.ForwardVector.ToPlain(),
                    OrientationUp = MySector.MainCamera.UpVector.ToPlain(),
                },
                JetpackRunning = Character.JetpackComp.TurnedOn,
                HeadLocalXAngle = Character.HeadLocalXAngle,
                HeadLocalYAngle = Character.HeadLocalYAngle,
                TargetBlock = TargetBlock(),
            };
        }

        private PlainVec3D PlayerExtent()
        {
            return Character.PositionComp.LocalAABB.Size.ToPlain();
        }

        private Block TargetBlock()
        {
            if (!(Character.CurrentWeapon is MyEngineerToolBase wp)) return null;
            var slimBlock = wp.GetTargetBlock();
            return slimBlock == null ? null : m_seEntityBuilder.CreateGridBlock(slimBlock);
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
            return new BoundingSphereD(GetPlayerPosition(), m_radius);
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

using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.Entities.Cube;
using System;
using System.Collections.Generic;
using System.Linq;
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

        private readonly LowLevelObserver m_lowLevelObserver;

        public Observer(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
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
                    observation.Blocks = m_lowLevelObserver.CollectSurroundingBlocks(sphere, mode);
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

                if (ivEntities.Count() > 1000) // TODO(PP): Define as param.
                {
                    Log?.WriteLine($"{nameof(CollectSurroundingEntities)}: Too many entities!");
                    break;
                }
            }

            return ivEntities;
        }
    }
}
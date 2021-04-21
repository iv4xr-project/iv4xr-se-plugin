using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage.FileSystem;
using VRage.Game.Entity;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public interface IObserver
    {
        SeObservation GetObservation();
        SeObservation GetObservation(ObservationArgs observationArgs);
        void TakeScreenshot(string absolutePath);
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
                    observation.Grids = m_lowLevelObserver.CollectSurroundingBlocks(sphere, mode);
                    break;

                default:
                    throw new ArgumentOutOfRangeException();
            }

            return observation;
        }

        public void TakeScreenshot(string absolutePath)
        {
            // Stolen from se/Sources/TestingToolPlugin/MyTestingToolPlugin.cs
            MyAsyncSaving.Start(null, Path.Combine(MyFileSystem.SavesPath, "..", "iv4XRtempsave"));
            MyGuiSandbox.TakeScreenshot(
                MySandboxGame.ScreenSize.X, MySandboxGame.ScreenSize.Y,
                absolutePath, true, false
            );
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

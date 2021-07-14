using System;
using System.IO;
using Iv4xr.PluginLib;
using Iv4xr.SePlugin.WorldModel;
using Sandbox;
using Sandbox.Game.Screens.Helpers;
using Sandbox.Graphics.GUI;
using VRage.FileSystem;

namespace Iv4xr.SePlugin.Control
{
    public interface IObserver
    {
        Observation Observe();
        Observation ObserveBlocks();
        Observation ObserveNewBlocks();
        void TakeScreenshot(string absolutePath);
    }

    internal class Observer : IObserver
    {
        public ILog Log { get; set; }

        public double Radius
        {
            get => m_lowLevelObserver.Radius;
            set => m_lowLevelObserver.Radius = value;
        }

        public static void ValidateRadius(double radius)
        {
            if (radius <= 0.0d)
                throw new Exception("Radius must be positive and non-zero.");
        }
        
        public const double DefaultRadius = 25.0d;

        private readonly LowLevelObserver m_lowLevelObserver;

        public Observer(LowLevelObserver lowLevelObserver)
        {
            m_lowLevelObserver = lowLevelObserver;
        }

        public Observation GetObservation(ObservationMode observationMode)
        {
            switch (observationMode)
            {
                case ObservationMode.BASIC:
                    return m_lowLevelObserver.GetBasicObservation();
                case ObservationMode.NEW_BLOCKS:
                    return m_lowLevelObserver.GetNewBlocks();
                case ObservationMode.BLOCKS:
                    return m_lowLevelObserver.GetBlocks();
                default:
                    throw new ArgumentOutOfRangeException("ObservationMode", observationMode.ToString());
            }
        }


        public Observation Observe()
        {
            return m_lowLevelObserver.GetBasicObservation();
        }

        public Observation ObserveBlocks()
        {
            return m_lowLevelObserver.GetBlocks();
        }

        public Observation ObserveNewBlocks()
        {
            return m_lowLevelObserver.GetNewBlocks();
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
    }
}

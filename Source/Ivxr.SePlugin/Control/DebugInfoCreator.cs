using System;
using System.Reflection;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Engine.Platform;
using Sandbox.Game.Multiplayer;
using SpaceEngineers.Game;
using VRage;

namespace Iv4xr.SePlugin.Control
{
    public static class DebugInfoCreator
    {
        public static DebugInfo Create()
        {
            var assembly = Assembly.GetEntryAssembly();
            return new DebugInfo()
            {
                IsDedicated = Game.IsDedicated,
                Executable = assembly.Location,
                Version = SpaceEngineersGame.SE_VERSION,
                MachineName = Environment.MachineName,
                SessionReady = MyVRage.Platform.SessionReady,
                MultiplayerActive = Sync.MultiplayerActive,
                IsServer = Sync.IsServer,
                UserName = Sync.MyName,
            };
        }
    }
}

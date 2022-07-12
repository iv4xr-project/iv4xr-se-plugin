namespace Iv4xr.SpaceEngineers.WorldModel
{
    public class SessionInfo
    {
        public string Name;
        public string Description;
        public string CurrentPath;
        public bool IsAdminMenuEnabled;
        public bool IsRunningExperimental;
        public bool Ready;
        public bool IsUnloading;
        public bool IsCopyPastingEnabled;
        public bool StreamingInProgress;
        public bool IsServer;
        public bool IsPausable;
        public DefinitionId GameDefinition;
        public SessionSettings Settings;
        public CameraController Camera;
    }

    public class CameraController
    {
        public bool IsInFirstPersonView;
        public bool ForceFirstPersonCamera;
        public Entity Entity;
        public CameraControllerEnum CameraControllerEnum;
    }

    public enum CameraControllerEnum
    {
        Spectator,
        Entity,
        ThirdPersonSpectator,
        SpectatorDelta,
        SpectatorFixed,
        SpectatorOrbit,
        SpectatorFreeMouse
    }

    public class SessionSettings
    {
        public GameModeEnum GameMode;
        public bool InfiniteAmmo;
    }

    public enum GameModeEnum
    {
        Creative,
        Survival,
    }
}

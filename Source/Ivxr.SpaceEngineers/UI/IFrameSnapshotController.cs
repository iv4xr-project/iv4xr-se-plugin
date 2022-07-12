namespace Iv4xr.SpaceEngineers.UI
{
    public interface IFrameSnapshotController
    {
        FrameSnapshot GetCurrent();
        void SetCurrent(FrameSnapshot snapshot);
    }
}

using Iv4xr.SePlugin.WorldModel;

namespace Iv4xr.SePlugin.Control
{
    public interface IObserver
    {
        Observation Observe();
        Observation ObserveBlocks();
        Observation ObserveNewBlocks();
        void TakeScreenshot(string absolutePath);
    }
}

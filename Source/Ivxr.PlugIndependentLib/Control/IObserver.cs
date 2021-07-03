using Iv4xr.PluginLib.WorldModel;

namespace Iv4xr.PluginLib.Control
{
    public interface IObserver
    {
        CharacterObservation Observe();
        Observation ObserveBlocks();
        Observation ObserveNewBlocks();
        void TakeScreenshot(string absolutePath);
    }
}

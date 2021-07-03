using Iv4xr.SePlugin.Session;

namespace Iv4xr.SePlugin.Control
{
    public interface ISpaceEngineers
    {
        ICharacterController Character { get; }
        ISessionController Session { get; }
        IItems Items { get; }
        IObserver Observer { get; }
        IDefinitions Definitions { get; }
    }
}

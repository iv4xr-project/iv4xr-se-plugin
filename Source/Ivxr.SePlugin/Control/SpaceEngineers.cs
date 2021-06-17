using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Config;
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

    public class RealSpaceEngineers : ISpaceEngineers
    {
        public ICharacterController Character { get; }
        public ISessionController Session { get; }
        public IItems Items { get; }
        public IObserver Observer { get; }
        public IDefinitions Definitions { get; }

        internal RealSpaceEngineers(GameSession gameSession, ILog log, PluginConfig config)
        {
            Observer = new Observer(new LowLevelObserver(gameSession) {Log = log})
            {
                Log = log,
                Radius = config.ObservationRadius
            };
            
            Character = new CharacterController(gameSession, Observer);
            Session = new SessionController() {Log = log};
            Items = new Items(gameSession);
            Definitions = new Definitions();
        }

        public RealSpaceEngineers(
            IObserver observer,
            ICharacterController controller,
            ISessionController sessionController,
            IItems items,
            IDefinitions definitions
        )
        {
            Observer = observer;
            Character = controller;
            Session = sessionController;
            Items = items;
            Definitions = definitions;
        }
    }
}

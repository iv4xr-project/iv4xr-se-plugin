using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Config;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox;
using VRage.Game;

namespace Iv4xr.SePlugin.Control
{
    public class RealSpaceEngineers : ISpaceEngineers
    {
        public ICharacterController Character { get; }
        public ISessionController Session { get; }
        public IItems Items { get; }
        public IObserver Observer { get; }
        public IDefinitions Definitions { get; }
        public ISpaceEngineersAdmin Admin { get; }
        public IScreens Screens { get; }

        public IBlocks Blocks { get; }

        internal RealSpaceEngineers(GameSession gameSession, ILog log, PluginConfig config)
        {
            var lowLevelObserver = new LowLevelObserver(gameSession) {Log = log};
            Observer = new Observer(lowLevelObserver)
            {
                Log = log,
                Radius = config.ObservationRadius
            };
            Session = new SessionController() {Log = log};
            Items = new Items(gameSession, lowLevelObserver);
            Definitions = new Definitions();
            var characterController = new CharacterController(gameSession, Observer, lowLevelObserver, log);
            Character = characterController;
            var blocks = new Blocks(gameSession, lowLevelObserver);
            Blocks = blocks;
            Admin = new SpaceEngineersAdmin(characterController, blocks, new ObserverAdmin(lowLevelObserver));
            Screens = new Screens();
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

    public class SpaceEngineersAdmin : ISpaceEngineersAdmin
    {
        public void SetFrameLimitEnabled(bool enabled)
        {
            MySandboxGame.Static.EnableMaxSpeed = !enabled;
        }

        public ICharacterAdmin Character { get; }
        public IBlocksAdmin Blocks { get; }
        public IObserverAdmin Observer { get; }
        public void UpdateDefaultInteractDistance(float distance)
        {
            MyConstants.DEFAULT_INTERACTIVE_DISTANCE = distance;
        }

        [RunOutsideGameLoop]
        public DebugInfo DebugInfo()
        {
            return DebugInfoCreator.Create();
        }

        public SpaceEngineersAdmin(ICharacterAdmin character, IBlocksAdmin blocks, IObserverAdmin observer)
        {
            Character = character;
            Blocks = blocks;
            Observer = observer;
        }
    }
}

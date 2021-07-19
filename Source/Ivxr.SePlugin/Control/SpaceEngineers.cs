using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SePlugin.Config;
using Sandbox;

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

        public IBlocks Blocks { get; }

        internal RealSpaceEngineers(GameSession gameSession, ILog log, PluginConfig config)
        {
            var lowLevelObserveer = new LowLevelObserver(gameSession) {Log = log};
            Observer = new Observer(lowLevelObserveer)
            {
                Log = log,
                Radius = config.ObservationRadius
            };
            Session = new SessionController() {Log = log};
            Items = new Items(gameSession, lowLevelObserveer);
            Definitions = new Definitions();
            var characterController = new CharacterController(gameSession, Observer);
            Character = characterController;
            var blocks = new Blocks(gameSession, lowLevelObserveer);
            Blocks = blocks;
            Admin = new SpaceEngineersAdmin(characterController, blocks);
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

        public SpaceEngineersAdmin(ICharacterAdmin character, IBlocksAdmin blocks)
        {
            Character = character;
            Blocks = blocks;
        }
    }
}

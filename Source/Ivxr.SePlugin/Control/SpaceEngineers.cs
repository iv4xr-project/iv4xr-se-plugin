using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Communication;
using Iv4xr.SePlugin.Config;
using Iv4xr.SePlugin.Control.Screen;
using Iv4xr.SePlugin.UI;
using Iv4xr.SpaceEngineers;
using Iv4xr.SpaceEngineers.UI;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox;
using Sandbox.Game.Gui;
using VRage.Game;
using static Iv4xr.SePlugin.Communication.CallTarget;

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
        public IInput Input { get; }

        public IBlocks Blocks { get; }
        public IDebug Debug { get; }

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
            Input = RealInput.Instance;
            Debug = new DebugData();
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
        public ITestAdmin Tests { get; }
        public void ShowNotification(string text)
        {
            MyHud.Notifications.Add(new MyHudNotificationDebug(text, 5000, level: MyNotificationLevel.Important));
        }

        public void UpdateDefaultInteractDistance(float distance)
        {
            MyConstants.DEFAULT_INTERACTIVE_DISTANCE = distance;
        }

        [CallOn(CurrentThread)]
        public DebugInfo DebugInfo()
        {
            return DebugInfoCreator.Create();
        }

        public SpaceEngineersAdmin(ICharacterAdmin character, IBlocksAdmin blocks, IObserverAdmin observer)
        {
            Character = character;
            Blocks = blocks;
            Observer = observer;
            Tests = new TestAdmin();
        }
    }
}

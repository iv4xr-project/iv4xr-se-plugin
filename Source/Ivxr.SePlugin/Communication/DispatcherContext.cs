using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;

namespace Iv4xr.SePlugin.Communication
{
    public class DispatcherContext
    {
        public readonly IObserver Observer;
        public readonly ICharacterController CharacterController;
        public readonly ISessionController SessionController;

        public DispatcherContext(IObserver observer, ICharacterController characterController,
            ISessionController sessionController)
        {
            Observer = observer;
            CharacterController = characterController;
            SessionController = sessionController;
        }
    }
}
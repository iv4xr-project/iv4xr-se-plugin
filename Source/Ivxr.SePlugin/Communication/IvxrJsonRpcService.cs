using AustinHarris.JsonRpc;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    public class IvxrJsonRpcService : JsonRpcService
    {
        private readonly IObserver m_observer;
        private readonly CharacterController m_characterController;
        private readonly ISessionController m_sessionController;

        public IvxrJsonRpcService(DispatcherContext dispatcherContext)
        {
            m_observer = dispatcherContext.Observer;
            m_characterController = dispatcherContext.CharacterController as CharacterController;
            m_sessionController = dispatcherContext.SessionController;
        }

        [JsonRpcMethod("Items.Equip")]
        public void Equip(ToolbarLocation toolbarLocation)
        {
            m_characterController.EquipToolbarItem(toolbarLocation.Slot, toolbarLocation.Page, false);
        }

        [JsonRpcMethod("Items.Place")]
        public void Place()
        {
            m_characterController.PlaceItem();
        }

        [JsonRpcMethod("Items.StartUsingTool")]
        public void StartUsingTool()
        {
            m_characterController.BeginUseTool();
        }

        [JsonRpcMethod("Items.EndUsingTool")]
        public void EndUsingTool()
        {
            m_characterController.EndUseTool();
        }

        [JsonRpcMethod("Items.SetToolbarItem")]
        public void SetToolbarItem(string name, ToolbarLocation location)
        {
            m_characterController.SetToolbarItem(location.Slot, location.Page, name);
        }

        [JsonRpcMethod("Character.MoveAndRotate")]
        public SeObservation MoveAndRotate(Vector3D movement, Vector2 rotation3, float roll)
        {
            m_characterController.Move(movement, rotation3, roll);
            return m_observer.GetObservation(new ObservationArgs {ObservationMode = ObservationMode.BASIC});
        }

        [JsonRpcMethod("Observer.Observe")]
        public SeObservation Observe()
        {
            return m_observer.GetObservation(ObservationArgs.Default);
        }

        [JsonRpcMethod("Observer.ObserveBlocks")]
        public SeObservation ObserveBlocks()
        {
            return m_observer.GetObservation(new ObservationArgs {ObservationMode = ObservationMode.BLOCKS});
        }

        [JsonRpcMethod("Observer.ObserveNewBlocks")]
        public SeObservation ObserveNewBlocks()
        {
            var observationArgs = new ObservationArgs {ObservationMode = ObservationMode.NEW_BLOCKS};
            return m_observer.GetObservation(observationArgs);
        }

        [JsonRpcMethod("Session.LoadScenario")]
        public void LoadScenario(string scenarioPath)
        {
            m_sessionController.LoadScenario(scenarioPath);
        }
    }
}
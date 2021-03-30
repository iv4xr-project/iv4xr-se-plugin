using AustinHarris.JsonRpc;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    public class ToolbarLocation
    {
        public int Page;
        public int Slot;
    }

    class Iv4xrJsonRpcService : JsonRpcService
    {
        private IObserver m_observer;
        private CharacterController m_characterController;
        private ISessionController m_sessionController;

        public Iv4xrJsonRpcService(IObserver observer, ICharacterController characterController,
            ISessionController sessionController)
        {
            m_observer = observer;
            m_characterController = characterController as CharacterController;
            m_sessionController = sessionController;
        }


        //{'method':'GetObservation','params':[],'id':1}
        [JsonRpcMethod]
        public SeObservation Observe()
        {
            return m_observer.GetObservation(ObservationArgs.Default);
        }

        [JsonRpcMethod]
        public void Equip(ToolbarLocation toolbarLocation)
        {
            m_characterController.Interact(new InteractionArgs
            {
                Page = toolbarLocation.Page,
                Slot = toolbarLocation.Slot,
                InteractionType = InteractionType.EQUIP,
                AllowSizeChange = false
            });
            m_observer.GetObservation();
        }

        [JsonRpcMethod]
        public void Place()
        {
            m_characterController.PlaceItem();
            m_observer.GetObservation();
        }

        [JsonRpcMethod]
        public void StartUsingTool()
        {
            m_characterController.BeginUseTool();
        }

        [JsonRpcMethod]
        public void EndUsingTool()
        {
            m_characterController.EndUseTool();
        }

        [JsonRpcMethod]
        public SeObservation MoveAndRotate(Vector3D movement, Vector2 rotation3, float roll)
        {
            m_characterController.Move(movement, rotation3, roll);
            return m_observer.GetObservation(new ObservationArgs {ObservationMode = ObservationMode.BASIC});
        }

        [JsonRpcMethod]
        public SeObservation ObserveBlocks()
        {
            return m_observer.GetObservation(new ObservationArgs {ObservationMode = ObservationMode.BLOCKS});
        }

        [JsonRpcMethod]
        public SeObservation ObserveNewBlocks()
        {
            var observationArgs = new ObservationArgs {ObservationMode = ObservationMode.NEW_BLOCKS};
            return m_observer.GetObservation(observationArgs);
        }

        //{'method':'LoadScenario','params':{'scenarioPath': 'C:/Users/karel.hovorka/git/iv4xrDemo-space-engineers/src/jvmTest/resources/game-saves/simple-place-grind-torch-with-tools'},'id':1}
        [JsonRpcMethod]
        public void LoadScenario(string scenarioPath)
        {
            m_sessionController.LoadScenario(scenarioPath);
        }
    }
}
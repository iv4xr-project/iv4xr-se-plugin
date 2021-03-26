using AustinHarris.JsonRpc;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.Session;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace Iv4xr.SePlugin.Communication
{
    class Iv4xrJsonRpcService : JsonRpcService, IObserver, ICharacterController, ISessionController
    {

        private IObserver m_observer;
        private ICharacterController m_characterController;
        private ISessionController m_sessionController;

        public Iv4xrJsonRpcService(IObserver observer, ICharacterController characterController,
            SessionController sessionController)
        {
            m_observer = observer;
            m_characterController = characterController;
            m_sessionController = sessionController;
        }

        [JsonRpcMethod] // handles JsonRpc like : {'method':'incr','params':[5],'id':1}
        private int incr(int i) { return i + 1; }


        //error: {'method':'decr','params':null,'id':1}
        //error: {'method':'decr','params':['x'],'id':1}
        [JsonRpcMethod] // handles JsonRpc like : {'method':'decr','params':[5],'id':1}
        private int decr(int i) { return i - 1; }

        //{'method':'GetObservation','params':[],'id':1}
        [JsonRpcMethod]
        public SeObservation GetObservation()
        {
            return m_observer.GetObservation();
        }
        
        //[JsonRpcMethod]
        public SeObservation GetObservation(ObservationArgs observationArgs)
        {
            return m_observer.GetObservation(observationArgs);
        }

        //[JsonRpcMethod]
        public void Move(Vector3 move, Vector2 rotation, float roll)
        {
            m_characterController.Move(move, rotation, roll);
        }

        [JsonRpcMethod]
        public void Move(MoveAndRotateArgs args)
        {
            m_characterController.Move(args);
        }

        [JsonRpcMethod]
        public void Interact(InteractionArgs args)
        {
            m_characterController.Interact(args);
        }

        //{'method':'LoadScenario','params':{'scenarioPath': 'C:/Users/karel.hovorka/git/iv4xrDemo-space-engineers/src/jvmTest/resources/game-saves/simple-place-grind-torch-with-tools'},'id':1}
        [JsonRpcMethod]
        public void LoadScenario(string scenarioPath)
        {
            m_sessionController.LoadScenario(scenarioPath);
        }
    }
}

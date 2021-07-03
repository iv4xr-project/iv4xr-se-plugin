using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace SeServerMock.Mocks
{
    internal class MockCharacterController : ICharacterController
    {
        public ILog Log { get; set; }
        
        public Observation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll)
        {
            throw new System.NotImplementedException();
        }

        public Observation Teleport(PlainVec3D position, PlainVec3D? orientationForward, PlainVec3D? orientationUp)
        {
            throw new System.NotImplementedException();
        }

        public Observation TurnOnJetpack()
        {
            throw new System.NotImplementedException();
        }

        public Observation TurnOffJetpack()
        {
            throw new System.NotImplementedException();
        }
    }
}

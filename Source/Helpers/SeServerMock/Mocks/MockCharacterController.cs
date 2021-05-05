using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;

namespace SeServerMock.Mocks
{
    internal class MockCharacterController : ICharacterController
    {
        public ILog Log { get; set; }
        
        public Observation MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll)
        {
            throw new System.NotImplementedException();
        }

        public Observation Teleport(Vector3 position)
        {
            throw new System.NotImplementedException();
        }
    }
}

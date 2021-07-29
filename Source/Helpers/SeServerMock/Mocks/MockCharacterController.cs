using System;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;

namespace SeServerMock.Mocks
{
    internal class MockCharacterController : ICharacterController
    {
        public ILog Log { get; set; }
        
        public CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll)
        {
            throw new NotImplementedException();
        }

        public CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward, PlainVec3D? orientationUp)
        {
            throw new NotImplementedException();
        }

        public CharacterObservation TurnOnJetpack()
        {
            throw new NotImplementedException();
        }

        public CharacterObservation TurnOffJetpack()
        {
            throw new NotImplementedException();
        }

        public CharacterObservation SwitchHelmet()
        {
            throw new NotImplementedException();
        }

        public void Use()
        {
            throw new NotImplementedException();
        }

        public void BeginUsingTool()
        {
            throw new NotImplementedException();
        }

        public void EndUsingTool()
        {
            throw new NotImplementedException();
        }

        public void Use()
        {
            throw new NotImplementedException();
        }
    }
}

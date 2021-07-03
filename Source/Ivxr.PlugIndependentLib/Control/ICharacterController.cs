using Iv4xr.SePlugin.WorldModel;

namespace Iv4xr.SePlugin.Control
{
    public interface ICharacterController
    {
        Observation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll = 0);
        Observation Teleport(PlainVec3D position, PlainVec3D? orientationForward = null, PlainVec3D? orientationUp = null);
        Observation TurnOnJetpack();
        Observation TurnOffJetpack();
    }
}

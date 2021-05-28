using System;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.World;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public interface ICharacterController
    {
        Observation MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll = 0);
        Observation Teleport(Vector3 position, Vector3? orientationForward = null, Vector3? orientationUp = null);
        Observation TurnOnJetpack();
        Observation TurnOffJetpack();
    }

    public class CharacterController : ICharacterController
    {
        private readonly IGameSession m_session;
        private readonly IObserver m_observer;

        public CharacterController(IGameSession session, IObserver observer)
        {
            m_session = session;
            m_observer = observer;
        }
        
        public Observation TurnOnJetpack()
        {
            Character.JetpackComp.TurnOnJetpack(true);
            return m_observer.Observe();
        }
        
        public Observation TurnOffJetpack()
        {
            Character.JetpackComp.TurnOnJetpack(false);
            return m_observer.Observe();
        }

        public Observation Teleport(Vector3 position, Vector3? orientationForward, Vector3? orientationUp)
        {
            if (orientationForward == null && orientationUp == null)
            {
                GetEntityController().ControlledEntity.Entity.PositionComp.SetPosition(position);
                return m_observer.Observe();
            }

            if (orientationForward == null || orientationUp == null)
            {
                throw new InvalidOperationException("Either both or none of the orientations are supposed to be set");
            }

            var matrix = MatrixD.CreateWorld(
                new Vector3D(position),
                orientationForward ?? Vector3.Zero,
                orientationUp ?? Vector3.Zero
            );
            GetEntityController().Player.Character.PositionComp.SetWorldMatrix(ref matrix);
            return m_observer.Observe();
        }
        
        public Observation MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll)
        {
            GetEntityController().ControlledEntity.MoveAndRotate(movement, rotation3, roll);
            return m_observer.Observe();
        }


        private MyEntityController GetEntityController()
        {
            if (m_session.Character is null)
                throw new NullReferenceException("I'm out of character!"); // Should not happen.

            var entityController = m_session.Character.ControllerInfo.Controller;

            if (entityController is null) // Happens when the character enters a vehicle, for example.
                throw new NotSupportedException("Entity control not possible now.");

            return entityController;
        }
        
        private MyCharacter Character => m_session.Character;
    }
}

using System;
using Iv4xr.SePlugin.WorldModel;
using Sandbox.Game.World;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public interface ICharacterController
    {
        Observation MoveAndRotate(Vector3 movement, Vector2 rotation3, float roll = 0);
        Observation Teleport(Vector3 position);
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
        
        public Observation Teleport(Vector3 position)
        {
            GetEntityController().ControlledEntity.Entity.PositionComp.SetPosition(position);
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
    }
}

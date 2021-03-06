﻿using System;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.World;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class CharacterController : ICharacterController, ICharacterAdmin
    {
        private readonly IGameSession m_session;
        private readonly IObserver m_observer;

        public CharacterController(IGameSession session, IObserver observer)
        {
            m_session = session;
            m_observer = observer;
        }
        
        public CharacterObservation TurnOnJetpack()
        {
            Character.JetpackComp.TurnOnJetpack(true);
            return m_observer.Observe();
        }
        
        public CharacterObservation TurnOffJetpack()
        {
            Character.JetpackComp.TurnOnJetpack(false);
            return m_observer.Observe();
        }

        public CharacterObservation Teleport(PlainVec3D position, PlainVec3D? orientationForward, PlainVec3D? orientationUp)
        {
            var vecPosition = new Vector3D(position.ToVector3());
            if (orientationForward == null && orientationUp == null)
            {
                GetEntityController().ControlledEntity.Entity.PositionComp.SetPosition(vecPosition);
                return m_observer.Observe();
            }

            if (orientationForward == null || orientationUp == null)
            {
                throw new InvalidOperationException("Either both or none of the orientations are supposed to be set");
            }

            var matrix = MatrixD.CreateWorld(
                vecPosition,
                orientationForward?.ToVector3() ?? Vector3.Zero,
                orientationUp?.ToVector3() ?? Vector3.Zero
            );
            GetEntityController().Player.Character.PositionComp.SetWorldMatrix(ref matrix);
            return m_observer.Observe();
        }
        
        public CharacterObservation MoveAndRotate(PlainVec3D movement, PlainVec2F rotation3, float roll)
        {
            GetEntityController().ControlledEntity.MoveAndRotate(movement.ToVector3(), rotation3.ToVector2(), roll);
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

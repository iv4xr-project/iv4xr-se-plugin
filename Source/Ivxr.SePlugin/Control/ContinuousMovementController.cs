using System.Collections.Generic;
using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities;

namespace Iv4xr.SePlugin.Control
{
    public class ContinuousMovementController
    {
        private readonly ILog m_log;

        private readonly Dictionary<long, ContinuousMovementContext> m_continuousMovementContextDictionary =
                new Dictionary<long, ContinuousMovementContext>();

        private readonly GameSession m_session;

        public ContinuousMovementController(ILog log, GameSession session)
        {
            m_log = log;
            m_session = session;
        }

        public void ChangeMovement(ContinuousMovementContext context)
        {
            m_continuousMovementContextDictionary[m_session.CurrentCharacterId] = context;
        }

        private ContinuousMovementContext CurrentMovementContext()
        {
            return m_continuousMovementContextDictionary.ContainsKey(m_session.CurrentCharacterId)
                    ? m_continuousMovementContextDictionary[m_session.CurrentCharacterId]
                    : null;
        }

        public void Tick()
        {
            var continuousMovementContext = CurrentMovementContext();
            if (continuousMovementContext == null || !continuousMovementContext.IsValid())
                return;

            continuousMovementContext.UseTick();
            var moveVector = continuousMovementContext.MoveVector.ToVector3();
            var rotateVector = continuousMovementContext.RotationVector.ToVector2();
            var entity = Entity();
            entity.MoveAndRotate(moveVector, rotateVector, continuousMovementContext.Roll);
        }

        private IMyControllableEntity Entity()
        {
            return m_session.Character.ControllerInfo.Controller.ControlledEntity;
        }
    }
}

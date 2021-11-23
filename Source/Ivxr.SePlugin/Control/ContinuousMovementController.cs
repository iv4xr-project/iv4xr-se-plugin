using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.SpaceEngineers.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.World;

namespace Iv4xr.SePlugin.Control
{
    public class ContinuousMovementController
    {
        private readonly ILog m_log;

        private ContinuousMovementContext m_continuousMovementContext = new ContinuousMovementContext();

        private GameSession m_session;

        public ContinuousMovementController(ILog log, GameSession session)
        {
            m_log = log;
            m_session = session;
        }

        public void ChangeMovement(ContinuousMovementContext context)
        {
            m_continuousMovementContext = context;
        }

        public void Tick()
        {
            if (!m_continuousMovementContext.IsValid())
                return;
            
            m_continuousMovementContext.UseTick();
            var moveVector = m_continuousMovementContext.MoveVector.ToVector3();
            var rotateVector = m_continuousMovementContext.RotationVector.ToVector2();
            var entity = Entity();
            entity.MoveAndRotate(moveVector, rotateVector, m_continuousMovementContext.Roll);
        }

        private IMyControllableEntity Entity()
        {
            return m_session.Character.ControllerInfo.Controller.ControlledEntity;
        }
    }
}

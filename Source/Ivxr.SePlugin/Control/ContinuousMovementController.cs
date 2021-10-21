using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Control;
using Iv4xr.PluginLib.WorldModel;
using Sandbox.Game.Entities;
using Sandbox.Game.World;

namespace Iv4xr.SePlugin.Control
{
    public class ContinuousMovementController
    {
        private readonly ILog m_log;

        private ContinuousMovementContext m_continuousMovementContext = new ContinuousMovementContext();

        public ContinuousMovementController(ILog log)
        {
            m_log = log;
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
            Entity().MoveAndRotate(moveVector, rotateVector, m_continuousMovementContext.Roll);
        }

        private IMyControllableEntity Entity()
        {
            return MySession.Static.ControlledEntity;
        }
    }
}

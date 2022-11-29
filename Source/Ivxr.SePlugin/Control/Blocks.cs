using System;
using Iv4xr.SpaceEngineers;
using Sandbox.Game.Entities;
using Sandbox.Game.World;
using VRage.Game.ModAPI;

namespace Iv4xr.SePlugin.Control
{
    public class Blocks : IBlocks
    {
        private readonly IGameSession m_session;
        private readonly LowLevelObserver m_observer;

        public Blocks(IGameSession session, LowLevelObserver observer)
        {
            m_session = session;
            m_observer = observer;
        }

        public void Place()
        {
            if (MySession.Static.IsAdminOrCreative())
            {
                if (MyCubeBuilder.Static is null)
                    throw new NullReferenceException("Cube builder is null.");

                MyCubeBuilder.Static.Add();
                return;
            }

            // else: use tool's primary action in the survival mode
            var entityController = GetEntityController();
            entityController.ControlledEntity.BeginShoot(MyShootActionEnum.PrimaryAction);
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

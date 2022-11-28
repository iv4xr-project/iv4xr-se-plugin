using System;
using Sandbox.Game.Entities;

namespace Iv4xr.SePlugin.Control
{
    public class AbstractBlockAdmin<T> where T : MyCubeBlock
    {
        protected readonly IGameSession m_session;
        protected readonly LowLevelObserver m_observer;

        public AbstractBlockAdmin(IGameSession session, LowLevelObserver observer)
        {
            m_session = session;
            m_observer = observer;
        }

        protected T BlockById(string id)
        {
            var block = m_observer.GetBlockById(id).FatBlock;
            if (block is T warhead)
            {
                return warhead;
            }

            throw new InvalidOperationException($"The block is not a {typeof(T)}, it is {block.DefinitionId}");
        }
    }
}

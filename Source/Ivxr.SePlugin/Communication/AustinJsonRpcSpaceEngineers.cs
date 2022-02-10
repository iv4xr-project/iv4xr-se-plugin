using AustinHarris.JsonRpc;
using Iv4xr.SpaceEngineers;

namespace Iv4xr.SePlugin.Communication
{
    public class AustinJsonRpcSpaceEngineers
    {
        private ISpaceEngineers m_se;

        public AustinJsonRpcSpaceEngineers(ISpaceEngineers se)
        {
            m_se = se;
        }
        
        private static void BindService<TType>(string sessionID, TType instance, string methodPrefix)
        {
            AustinUtil.BindService<TType>(sessionID, instance, methodPrefix);
        }

        public void Bind()
        {
            var sessionId = Handler.DefaultSessionId();
            
            BindService<ICharacterController>(sessionId, m_se.Character, "Character.");
            BindService<IItems>(sessionId, m_se.Items, "Items.");
            BindService<IObserver>(sessionId, m_se.Observer, "Observer.");
            BindService<ISessionController>(sessionId, m_se.Session, "Session.");
            BindService<IDefinitions>(sessionId, m_se.Definitions, "Definitions.");
            BindService<IBlocks>(sessionId, m_se.Blocks, "Blocks.");

            BindService<ISpaceEngineersAdmin>(sessionId, m_se.Admin, "Admin.");
            BindService<IBlocksAdmin>(sessionId, m_se.Admin.Blocks, "Admin.Blocks.");
            BindService<ICharacterAdmin>(sessionId, m_se.Admin.Character, "Admin.Character.");
            BindService<IObserverAdmin>(sessionId, m_se.Admin.Observer, "Admin.Observer.");

            BindService<IScreens>(sessionId, m_se.Screens, "Screens.");
            BindService<IMedicals>(sessionId, m_se.Screens.Medicals, "Screens.Medicals.");
        }
    }
}

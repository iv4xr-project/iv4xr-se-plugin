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
        
        public void Bind()
        {
            var sessionId = Handler.DefaultSessionId();
            AustinUtil.BindRecursively<ISpaceEngineers>(sessionId, m_se, "", typeof(ISpaceEngineers));
        }
    }
}

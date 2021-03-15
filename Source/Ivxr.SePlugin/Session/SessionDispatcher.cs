using Iv4xr.PluginLib;
using Iv4xr.PluginLib.Comm;
using Iv4xr.SePlugin.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace Iv4xr.SePlugin.Session
{
    public class SessionDispatcher : ISessionDispatcher
    {
        public ILog Log { get; set; }

        private readonly Jsoner m_jsoner = new Jsoner();

        private readonly ISessionController m_sessionController;

        public SessionDispatcher(ISessionController sessionController)
        {
            m_sessionController = sessionController;
        }

        public void ProcessRequest(RequestItem request)
        {
            try
            {
                var requestShell = m_jsoner.ToObject<SeRequestShell<SessionCommand>>(request.Message);

                m_sessionController.LoadScenario(requestShell.Arg.ScenarioPath);
                PluginServer.ReplyOK(request.ClientStreamWriter);
            }
            catch (Exception ex)
            {
                Log.Exception(ex, "Error processing a request");
                Log.WriteLine($"Full request: \"{request.Message}\"");
                PluginServer.ReplyFalse(request
                        .ClientStreamWriter); // Simple error response, details can be learned from the log.
            }
        }
    }
}
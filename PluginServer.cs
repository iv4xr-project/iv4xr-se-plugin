using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using VRage.Utils;

namespace EU.Iv4xr.SePlugin
{
    class PluginServer
    {
        private bool m_shouldStop;

        private MyLog m_log;
        
        public PluginServer()
        {
            m_log = IvxrPlugin.Log;
        }

        public void Start()
        {
            var thread = new Thread(() =>
            {
                while (!m_shouldStop)
                {
                    Thread.Sleep(250);
                    m_log.WriteLine("tick");
                }

                m_log.WriteLine("Ivxr server loop ended");
            })
            {
                IsBackground = true,
                Name = "Ivrx plugin server thread"
            };
            thread.Start();
        }

        public void Stop()
        {
            m_shouldStop = true;
        }
	}
}

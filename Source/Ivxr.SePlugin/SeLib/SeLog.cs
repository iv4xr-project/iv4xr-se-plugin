using System.Text;
using Iv4xr.PluginLib;
using VRage.Utils;

namespace Iv4xr.SePlugin.SeLib
{
    class SeLog : ILog
    {
        private MyLog m_log;

        private string m_version;

        public SeLog(string version, bool alwaysFlush = false)
        {
            m_log = new MyLog(alwaysFlush);
            m_version = version;
        }

        public void Init(string logFileName)
        {
            m_log.Init(logFileName, new StringBuilder(m_version));
        }

        public void WriteLine(string message)
        {
            m_log.WriteLine(message);
        }
    }
}

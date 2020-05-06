using System;
using System.Collections.Generic;
using System.Text;

using EU.Iv4xr.PluginLib;
using VRage.Utils;

namespace EU.Iv4xr.SePlugin.SeLib
{
	class SeLog : ILog
	{
		private MyLog m_log;

		public SeLog(bool alwaysFlush = false)
		{
			m_log = new MyLog(alwaysFlush);
		}

		public void Init(string logFileName)
		{
			m_log.Init(logFileName, new StringBuilder("0.1.0"));
		}

		public void WriteLine(string message)
		{
			m_log.WriteLine(message);
		}
	}
}

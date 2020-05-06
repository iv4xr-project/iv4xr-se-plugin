using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Text;

namespace EU.Iv4xr.PluginLib.Log
{
	public class ConsoleLog : ILog
	{
		public void WriteLine(string message)
		{
			Console.WriteLine(message);
		}
	}
}

using System;
using System.Collections.Generic;
using System.Text;

namespace Iv4xr.PluginLib
{
	public interface ILog
	{
		void WriteLine(string message);
	}

	public static class LogExtensions
	{
		public static void Exception(this ILog log, Exception ex, string message)
		{
			log.WriteLine($"{message}: {ex.Message}");
			log.WriteLine($"Exception details:\n{ex.ToString()}\n-----\n");
		}
	}
}

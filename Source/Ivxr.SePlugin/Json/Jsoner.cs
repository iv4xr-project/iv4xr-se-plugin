using System;
using System.Collections.Generic;
using System.Text;
using LitJson;

namespace Iv4xr.SePlugin.Json
{
	public class Jsoner
	{
		private readonly JsonWriter m_writer = new JsonWriter();

		public string ToJson(object obj)
		{
			m_writer.Reset();

			JsonMapper.ToJson(obj, m_writer);

			return m_writer.ToString();
		}
	}
}

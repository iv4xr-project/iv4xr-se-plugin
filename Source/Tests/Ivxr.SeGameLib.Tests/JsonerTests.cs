using Iv4xr.SeGameLib.Json;
using System;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
	public class JsonerTests
	{
		private readonly Jsoner m_jsoner = new Jsoner();

		private class Class
		{
			public int Number = 9;
			public string Message = "Dragon lands.";
		}

		[Fact]
		public void ConvertsObjectToJson()
		{
			var obj = new Class();
			
			Assert.Equal("{\"Number\":9,\"Message\":\"Dragon lands.\"}", m_jsoner.ToJson(obj));
		}

	}
}

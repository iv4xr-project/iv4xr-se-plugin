using Iv4xr.SeGameLib.Json;
using System;
using EU.Iv4xr.SeGameLib.WorldModel;
using Iv4xr.SeGameLib.WorldModel;
using VRageMath;
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

		[Fact]
		public void ConvertsSeObservationToJson()
		{
			var observation = new SeObservation {AgentID = "Foo", Position = new PlainVec3D(1, 2, 3)};

			var json = m_jsoner.ToJson(observation);

			Assert.Equal("{\"AgentID\":\"Foo\"," +
			             "\"Position\":{\"X\":1.0,\"Y\":2.0,\"Z\":3.0}," +
			             "\"Velocity\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
			             "\"Extent\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}}",
				json);
		}

	}
}

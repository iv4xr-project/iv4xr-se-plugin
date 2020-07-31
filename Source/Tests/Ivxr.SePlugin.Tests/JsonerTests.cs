using Iv4xr.SePlugin.Json;
using System;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;
using Xunit;
using System.Collections.Generic;
using Iv4xr.PluginLib.Comm;

namespace Ivxr.SeGameLib.Tests
{
	public class JsonerTests
	{
		private readonly Jsoner m_jsoner = new Jsoner();

		private const int Precision = 4;

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
			var observation = new SeObservation
			{
				AgentID = "Foo",
				Position = new PlainVec3D(1, 2, 3),
				Entities = new List<SeEntity>()
			};

			var json = m_jsoner.ToJson(observation);

			Assert.Equal("{\"AgentID\":\"Foo\"," +
			             "\"Position\":{\"X\":1.0,\"Y\":2.0,\"Z\":3.0}," +
			             "\"Velocity\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
			             "\"Extent\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0}," +
						 "\"Entities\":[]}",
				json);
		}

		[Fact]
		public void DeserializesPlainVec3DIntoVector3D()
		{
			var originalVector = new PlainVec3D(1, 2, -3);

			var json = m_jsoner.ToJson(originalVector);

			var vector3 = m_jsoner.ToObject<Vector3D>(json);

			Assert.Equal(originalVector.X, vector3.X);
			Assert.Equal(originalVector.Y, vector3.Y);
			Assert.Equal(originalVector.Z, vector3.Z);
		}

		[Fact]
		public void DeserializesAgentCommandRequest()
		{
			string requestJson = "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\"MOVETOWARD\"," +
				"\"AgentId\":\"you\",\"TargetId\":\"you\"," +
				"\"Arg\":5.5}}";

			var request = m_jsoner.ToObject<SeRequestShell<AgentCommand<double>>>(requestJson);

			Assert.Equal(5.5, request.Arg.Arg, Precision);
		}

		[Fact]
		public void DeserializesMoveCommandArgs()
		{
			string argsJson = "{\"Object1\":{\"X\":0.0,\"Y\":0.0,\"Z\":-1.0},\"Object2\":false}";

			var args = m_jsoner.ToObject<MoveCommandArgs>(argsJson);

			Assert.Equal(-1.0d, args.MoveIndicator.Z, Precision);
		}

		[Fact]
		public void DeserializesMoveCommand()
		{
			string requestJson = "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\"MOVETOWARD\"," +
				"\"AgentId\":\"you\",\"TargetId\":\"you\"," +
				"\"Arg\":{\"Object1\":{\"X\":0.0,\"Y\":0.0,\"Z\":-1.0},\"Object2\":false}}}";

			var request = m_jsoner.ToObject<SeRequestShell<AgentCommand<MoveCommandArgs>>>(requestJson);

			Assert.Equal(-1.0d, request.Arg.Arg.MoveIndicator.Z, Precision);
		}

		[Fact]
		public void DeserializesMoveAndRotateCommand()
		{
			string requestJson = "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\"MOVE_ROTATE\"," +
				"\"AgentId\":\"you\",\"TargetId\":\"you\"," +
				"\"Arg\":{\"Movement\":{\"X\":0.0,\"Y\":0.0,\"Z\":-1.0},\"Rotation3\":{\"X\":0.3,\"Y\":0.0,\"Z\":0.0},\"Roll\":0.0}}}";

			var request = m_jsoner.ToObject<SeRequestShell<AgentCommand<MoveAndRotateArgs>>>(requestJson);

			var args = request.Arg.Arg;
			Assert.Equal(-1.0f, args.Movement.Z, Precision);
			Assert.Equal(0.3f, args.Rotation.X, Precision);  // Check for correct mapping Rotation3 -> Rotation
			Assert.Equal(0.0f, args.Rotation.Y, Precision);
		}

		/* (don't even show this in the list)
		[Fact(Skip="One time use.")]
		public void MeasureCommandPrefixLength()
		{
			string commandPrefix = "{\"Cmd\":\"AGENTCOMMAND\",\"Arg\":{\"Cmd\":\"";

			Assert.Equal(36, commandPrefix.Length);  // Need this constant in the code.
		}
		*/
	}
}

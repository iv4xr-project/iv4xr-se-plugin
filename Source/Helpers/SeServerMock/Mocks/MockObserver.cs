using System;
using System.Collections.Generic;
using System.Text;
using Iv4xr.SePlugin.WorldModel;
using Iv4xr.SePlugin.Control;
using VRage.Noise.Modifiers;

namespace SeServerMock.Mocks
{
	public class MockObserver : IObserver
	{
		public SeObservation GetObservation()
		{
			var entity = new SeEntity()
			{
				Id = "Ente",
				Position = new PlainVec3D(3, 2, 1)
			};

			var entities = new List<SeEntity>
			{
				entity
			};

			var block = new SeBlock()
			{
				Id = "blk",
				Position = new PlainVec3D(5, 5, 5),
				Integrity = 5.0f
			};

			var blocks = new List<SeBlock>
			{
				block
			};

			return new SeObservation()
			{
				AgentID = "Mock",
				Position = new PlainVec3D(4, 2, 0),
				Entities = entities,
				Blocks = blocks
			};
		}
	}
}

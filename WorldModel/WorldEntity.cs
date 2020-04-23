using ParallelTasks;
using System;
using System.Collections.Generic;
using System.Text;

namespace EU.Iv4xr.SePlugin.WorldModel
{
	class WorldEntity
	{
		public readonly string id;
		public readonly string name;

		public WorldEntity(string name)
		{
			id = "random";
			this.name = name;
		}
	}
}

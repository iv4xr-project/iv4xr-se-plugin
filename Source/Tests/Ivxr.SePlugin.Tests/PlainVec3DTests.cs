using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Iv4xr.SePlugin.Json;
using Iv4xr.SePlugin.WorldModel;
using VRageMath;
using Xunit;

namespace Ivxr.SeGameLib.Tests
{
	public class PlainVec3DTests
	{
		[Fact]
		public void ConvertsCorrectlyFromVector3D()
		{
			var vector3D = new Vector3D(1, 2, 3);

			var plainVec = new PlainVec3D(1, 2, 3);
			var convertedPlainVec = new PlainVec3D(vector3D);

			var jsoner = new Jsoner();

			Assert.Equal(jsoner.ToJson(plainVec), jsoner.ToJson(convertedPlainVec));
		}
	}
}

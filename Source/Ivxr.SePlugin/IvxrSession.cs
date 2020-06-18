using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Havok;
using Sandbox.Game.Entities;
using VRage.Game.Components;
using VRageMath;
using Sandbox.Game.Multiplayer;
using VRage.Game.Entity;
using LitJson;
using EU.Iv4xr.SePlugin.WorldModel;

namespace EU.Iv4xr.SePlugin
{
    [MySessionComponentDescriptor(MyUpdateOrder.BeforeSimulation | MyUpdateOrder.AfterSimulation)]
    class IvxrSession : MySessionComponentBase
    {
        public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();

            IvxrPlugin.Controller.ProcessRequests();
        }

		protected override void UnloadData()
		{
			base.UnloadData();

			IvxrPlugin.Context.EndSession();
		}
	}
}

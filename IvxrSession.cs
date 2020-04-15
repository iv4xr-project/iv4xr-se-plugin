using System;
using System.Collections.Generic;
using System.Text;
using VRage.Game.Components;

namespace EU.Iv4xr.SePlugin
{
    [MySessionComponentDescriptor(MyUpdateOrder.BeforeSimulation | MyUpdateOrder.AfterSimulation)]
    class IvxrSession : MySessionComponentBase
    {
        public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();
            IvxrPlugin.Log.WriteLine("IvxrSession: *** Update before simulation ***");
        }
    }
}

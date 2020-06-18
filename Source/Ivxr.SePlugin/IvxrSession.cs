using VRage.Game.Components;

namespace Iv4xr.SePlugin
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

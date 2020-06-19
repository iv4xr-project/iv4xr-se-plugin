using VRage.Game.Components;

namespace Iv4xr.SePlugin
{
	[MySessionComponentDescriptor(MyUpdateOrder.BeforeSimulation | MyUpdateOrder.AfterSimulation)]
    class IvxrSessionComponent : MySessionComponentBase
    {
		public override void BeforeStart()
		{
			base.BeforeStart();

			IvxrPlugin.Context.InitSession();
		}

		public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();

            IvxrPlugin.Dispatcher.ProcessRequests();
        }

		protected override void UnloadData()
		{
			base.UnloadData();

			IvxrPlugin.Context.EndSession();
		}
	}
}

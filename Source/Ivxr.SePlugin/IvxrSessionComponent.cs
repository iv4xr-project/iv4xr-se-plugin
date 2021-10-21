using VRage.Game.Components;

namespace Iv4xr.SePlugin
{
    [MySessionComponentDescriptor(MyUpdateOrder.BeforeSimulation | MyUpdateOrder.AfterSimulation)]
    class IvxrSessionComponent : MySessionComponentBase
    {
        private IvxrPluginContext Context => IvxrPlugin.Context;

        public override void BeforeStart()
        {
            base.BeforeStart();

            Context.InitSession();
        }

        public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();
            Context.FuncActionDispatcher.CallEverything();
            Context.ContinuousMovementController.Tick();
        }

        protected override void UnloadData()
        {
            base.UnloadData();

            Context.EndSession();
        }
    }
}

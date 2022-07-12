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
            Context?.BeforeStart();
        }

        public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();
            Context?.BeforeSimulation();
        }

        public override void UpdateAfterSimulation()
        {
            base.UpdateAfterSimulation();
            Context?.AfterSimulation();
        }


        protected override void UnloadData()
        {
            base.UnloadData();
            Context?.EndSession();
        }
    }
}

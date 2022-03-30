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
            if (Context != null)
            {
                Context.InitSession();
            }
        }

        public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();
            if (Context != null)
            {
                Context.BeforeSimulation();
                Context.MethodCallContext.BeforeSimulation.CallEverything();
            }
        }

        public override void UpdateAfterSimulation()
        {
            base.UpdateAfterSimulation();
            if (Context != null)
            {
                Context.MethodCallContext.AfterSimulation.CallEverything();
            }
        }


        protected override void UnloadData()
        {
            base.UnloadData();
            if (Context != null)
            {
                Context.EndSession();
            }
        }
    }
}

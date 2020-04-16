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

namespace EU.Iv4xr.SePlugin
{
    [MySessionComponentDescriptor(MyUpdateOrder.BeforeSimulation | MyUpdateOrder.AfterSimulation)]
    class IvxrSession : MySessionComponentBase
    {
        public override void UpdateBeforeSimulation()
        {
            base.UpdateBeforeSimulation();

            var playerCount = Sync.Players.GetOnlinePlayerCount();
            if (playerCount > 0)
            {
                var player = Sync.Players.GetOnlinePlayers().First();

                var characterPosition = player.Character.PositionComp.GetPosition();
                
                var sphere = new BoundingSphereD(characterPosition, radius: 25.0);
                List<MyEntity> entities = MyEntities.GetEntitiesInSphere(ref sphere);

                IvxrPlugin.Log.WriteLine(
                    $"IvxrSession: position: {characterPosition.X}, entities count: {entities.Count}");
                
                entities.Clear();
            }
        }
    }
}

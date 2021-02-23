using Iv4xr.PluginLib;
using Iv4xr.SePlugin.Control;
using Iv4xr.SePlugin.WorldModel;
using System;
using System.Collections.Generic;
using System.Text;
using VRageMath;

namespace SeServerMock.Mocks
{
    internal class MockCharacterController : ICharacterController
    {
        public ILog Log { get; set; }

        public void Interact(InteractionArgs args)
        {
            Log?.WriteLine($"Interaction type: {args.InteractionType}");
            Log?.WriteLine($"Slot: {args.Slot}");
        }

        public void Move(Vector3 move, Vector2 rotation, float roll)
        {
            // Noop.
        }

        public void Move(MoveAndRotateArgs args)
        {
            // Noop.
        }
    }
}
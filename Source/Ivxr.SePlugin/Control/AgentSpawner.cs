using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.World;
using Sandbox.ModAPI;
using VRage.Game;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public class MyAgentIdentity : MyIdentity.Friend
    {
        public override MyIdentity CreateNewIdentity(MyObjectBuilder_Identity objectBuilder)
        {
            return base.CreateNewIdentity(objectBuilder);
        }
    }


    public static class AgentSpawner
    {
        public static MyCharacter SpawnAgent(ulong steamId, string name, Color color, MatrixD startPosition,
            long identityId = 1000)
        {
            var character = MyCharacter.CreateCharacter(
                startPosition, Vector3.Zero, name, "", color, null, identityId: identityId);
            character.SwitchJetpack();
            
            var playerId = new MyPlayer.PlayerId(steamId);

            var identityBuilder = new MyObjectBuilder_Identity
            {
                DisplayName = name,
                CharacterEntityId = character.EntityId,
                ColorMask = color.ToVector3()
            };
            var identity = new MyAgentIdentity().CreateNewIdentity(identityBuilder);
            
            var myPlayer = new MyPlayer(Sync.Clients.LocalClient, playerId)
            {
                Identity = identity
            };
            var characterController = new MyEntityController(myPlayer);

            characterController.TakeControl(character);

            return character;
        }
    }
}

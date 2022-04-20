using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using Sandbox.Game.World;
using Sandbox.ModAPI;
using VRage.Game;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public static class AgentSpawner
    {
        public static MyCharacter SpawnAgent(
            ulong steamId, string name, Color color, MatrixD startPosition, long identityId
        )
        {
            var identityBuilder = new MyObjectBuilder_Identity
            {
                DisplayName = name,
                ColorMask = color.ToVector3(),
                IdentityId = identityId,
            };

            var identity = MySession.Static.Players.CreateNewIdentity(identityBuilder);

            var character = MyCharacter.CreateCharacter(
                startPosition, Vector3.Zero, name, "", color, null, identityId: identityId);


            var client = new MyNetworkClient(steamId, name);
            //client.SetInstanceProperty("IsLocal", true);

            var player = MySession.Static.Players.CreateNewPlayer(
                identity: identity, steamClient: client, playerName: name,
                realPlayer: true
            );

            var characterController = new MyEntityController(player);

            characterController.TakeControl(character);

            return player.Character;
        }
    }
}

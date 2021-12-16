using System;
using System.Collections.Generic;
using Sandbox.Game.Entities.Character;
using Sandbox.Game.World;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public interface IGameSession
    {
        MyCharacter Character { get; }
        void SetCharacter(long characterId);

        MyCharacter CreateCharacter(string name, Vector3D position, Vector3D orientationForward,
            Vector3D orientationUp);

        long CurrentCharacterId { get; }
    }

    public class GameSession : IGameSession
    {
        private const long NoCharacter = -1;
        private readonly Dictionary<long, MyCharacter> m_characters = new Dictionary<long, MyCharacter>();

        public long CurrentCharacterId { get; private set; } = NoCharacter;

        public void SetCharacter(long characterId)
        {
            if (!m_characters.ContainsKey(characterId))
            {
                throw new InvalidOperationException("character with id not found: " + characterId);
            }

            CurrentCharacterId = characterId;
        }

        public MyCharacter CreateCharacter(string name, Vector3D position, Vector3D orientationForward,
            Vector3D orientationUp)
        {
            var matrix = MatrixD.CreateWorld(position: position, forward: orientationForward, up: orientationUp);
            //TODO: steamId, color
            var character = AgentSpawner.SpawnAgent(
                12345, name: name, color: Color.White, startPosition: matrix);
            var characterId = character.CharacterId();
            m_characters[characterId] = character;
            CurrentCharacterId = characterId;
            return character;
        }

        public void RemoveCharacter(long characterId)
        {
            if (!m_characters.ContainsKey(characterId))
            {
                throw new InvalidOperationException("character with id not found: " + characterId);
            }

            m_characters.Remove(characterId);
            //TODO: remove from the game            
        }

        public MyCharacter Character
        {
            get
            {
                if (m_characters.Count == 0)
                {
                    InitSession();
                }

                return m_characters[CurrentCharacterId];
            }
        }

        public void InitSession()
        {
            var character = MySession.Static.LocalCharacter;
            var characterId = character.CharacterId();
            CurrentCharacterId = characterId;
            m_characters[characterId] = character;
        }

        public void EndSession()
        {
            CurrentCharacterId = NoCharacter;
            m_characters.Clear();
        }
    }
}

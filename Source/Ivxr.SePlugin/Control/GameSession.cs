using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Sandbox.Game.World;
using VRageMath;

namespace Iv4xr.SePlugin.Control
{
    public interface IGameSession
    {
        MyCharacter Character { get; }
        void SetCharacter(string characterId);

        MyCharacter CreateCharacter(string name, Vector3D position, Vector3D orientationForward,
            Vector3D orientationUp);

        string CurrentCharacterId { get; }
    }

    public class GameSession : IGameSession
    {
        private readonly Dictionary<string, MyCharacter> m_characters = new Dictionary<string, MyCharacter>();

        public string CurrentCharacterId { get; private set; } = null;

        public void SetCharacter(string characterId)
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
            var characterId = character.EntityId.ToString();
            m_characters[characterId] = character;
            CurrentCharacterId = characterId;
            return character;
        }

        public void RemoveCharacter(string characterId)
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
            var characterId = character.EntityId.ToString();
            CurrentCharacterId = characterId;
            m_characters[characterId] = character;
        }

        public void EndSession()
        {
            CurrentCharacterId = null;
            m_characters.Clear();
        }
    }
}

using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Iv4xr.SePlugin.Control
{
	public interface IGameSession
	{
		MyCharacter Character { get; }
	}

	internal class GameSession : IGameSession
	{
		public MyCharacter Character
		{
			get
			{
				if (m_character is null)
					InitSession();

				return m_character;
			}
		}
		private MyCharacter m_character;

		public void InitSession()
		{
			var playerCount = Sync.Players.GetOnlinePlayerCount();
			if (playerCount <= 0)
			{
				throw new InvalidOperationException("Didn't find any players.");
			}

			m_character = Sync.Players.GetOnlinePlayers().First().Character;
		}

		public void EndSession()
		{
			m_character = null;
		}
	}
}

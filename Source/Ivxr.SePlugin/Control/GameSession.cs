using Sandbox.Game.Entities.Character;
using Sandbox.Game.Multiplayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Iv4xr.SePlugin.Control
{
	internal class GameSession
	{
		public MyCharacter Character { get; private set; }

		public void InitSession()
		{
			var playerCount = Sync.Players.GetOnlinePlayerCount();
			if (playerCount <= 0)
			{
				throw new InvalidOperationException("Didn't find any players.");
			}

			Character = Sync.Players.GetOnlinePlayers().First().Character;
		}

		public void EndSession()
		{
			Character = null;
		}
	}
}

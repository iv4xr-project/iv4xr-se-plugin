# Using plugin in SE multiplayer

## Overview

It is possible to control multiple game instances at once, each of them having the plugin installed. That means managing
connection for every plugin separately.

## About multiplayer

There are 2 basic types of multiplayer in SE:

- Lobby - game is created by the game itself and one of the players is "host" of the game.
- Dedicated server (DS) - game is created by headless dedicated server. Dedicated server is not involved as player.

The dedicated server and single game instance can run both on same machine. Just make sure you change the plugin port on
either of them.

## Setup

- Make sure your `Hostname` and port (`JsonRpcPort`) configuration is correct, check your `ivxr-plugin.config` file,
  located either in
  `~\AppData\Roaming\SpaceEngineers` or `~\AppData\Roaming\SpaceEngineersDedicated`.
- Hostname IP should match your network interface. If you don't care about interface setup, just set it to `0.0.0.0` (
  all interfaces). This has to be done, especially if your current (default) setting is `127.0.0.1`, which allows
  connections only from your local computer.
- If you are somehow using multiple plugins at single operating system (virtualization or using game + dedicated server
  at once), make sure that every instance has its own port.
- You can use `nmap`, `telnet` or other networking tools to check ports being opened.

## Loading game scenarios

### Lobby

Do not use `Session.LoadScenario`, use `Screens.LoadGame.*` screen to load a scenario.

### Dedicated server

There is no extra code to help with creating DS games, but it is possible to start a dedicated server directly with a
scenario to load, example:

```
SpaceEngineersDedicated.exe session:${scenarioPath} -plugin Ivxr.SePlugin.dll -console -start
```

To kill DS by force, use:

```
C:\windows\system32/taskkill /IM SpaceEngineersDedicated.exe /F
```

## Joining games

## Admin API

Admin commands work only on the host, who created the game, which is either a lobby host or a dedicated server. Calling
admin commands on a game client will either raise exception or show unexpected results (we are trying to put exceptions
everywhere).

## Screen API

The screen API doesn't work on dedicated server, because it has no game screens and no GUI.

## Multiplayer delay

Commands in series work a lot better in single player and when switching to multiplayer, series can start failing,
because the actions take longer to show (ex. placing block). Adding some delays or waits until the game state changes
will help.

## Synchronized values

Some values are not being synchronized over network so even though API is returning values, they are set to default. We
don't have definite list (TODO), but it is mostly logical and for performance reasons so just properties not required
for clients to know are not being synchronized.

Some examples of synchronized character properties:

- Position and orientation.
- Helmet state.
- Inventory.

Some examples of NOT synchronized properties:

- Character dampener status.
- Current gravity on character.


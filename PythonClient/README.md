# Client implementation in Python

This is a simple implementation of [Space Engineers](https://www.spaceengineersgame.com/)
iv4XR [plugin](https://github.com/iv4xr-project/iv4xr-se-plugin) client. It can be used to control the Space Engineers
game.

The purpose is not to create a full-featured client, but to make it easier to work with Space Engineers using python.

As an alternative there is [client in kotlin](../JvmClient).

## Installation

The client itself doesn't have any dependencies, but some of the examples do:

- [Maze generator example](src/examples/maze.py) uses a great maze generator
  library [mazelib](https://github.com/john-science/mazelib)
- Cucumber test uses [behave](https://behave.readthedocs.io/en/stable/)

If you want to install those dependencies, simply install them through pip or use:

```
pip install -r requirements.txt
```

## Basic usage:

To use the client, you have to have Space Engineers game
with a [running iv4XR plugin](../README.md#how-to-run-the-game-with-this-plugin).

To connect to a local game with a running plugin:

```
from spaceengineers.proxy import SpaceEngineersProxy
se = SpaceEngineersProxy.localhost()
# Example - go to "Load game" screen (game has to be in the main menu):
se.Screens.MainMenu.LoadGame()
```

See [examples directory](src/examples) for more.

## Limitations

- All API calls have to be made either with all arguments named or all arguments positional (named arguments are
  recommended). Mixture is not allowed and will cause an error.
- All default values have to be provided.

## Background

It is mostly wrapper around [JSON-RPC](https://www.jsonrpc.org/specification)
protocol. [models.py](src/spaceengineers/models.py)
and [api.py](src/spaceengineers/api.py) are generated by kotlin client. To add new API you don't need to modify those,
they are purely for type safety/code completion IDE hints.

## Cucumber implementation

The project includes a single feature file [C284491.feature](src/features/C284491.feature). This file has been copied from
kotlin client. By implementing steps it showcases re-usability of gherkin (simpler than in the Kotlin client - works only
for single-player).

## Useful links

- [Kotlin client](../JvmClient)
- [Basics](../JvmClient/docs/Basics.MD)
- [Blocks](../JvmClient/docs/Blocks.MD)
- [Movement](../JvmClient/docs/Movement.MD)
- [Screens](../JvmClient/docs/Screens.MD)
- [Automated testing](../JvmClient/docs/Automated-Testing.MD)
- [Multiplayer](../JvmClient/docs/Multiplayer.MD)
- [Ownership](../JvmClient/docs/Ownership.MD)
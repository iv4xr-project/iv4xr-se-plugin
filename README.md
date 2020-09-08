# iv4xr-se-plugin
Integration of **[Space Engineers](https://www.spaceengineersgame.com/)** to the **iv4XR framework**. You can find the project page at [iv4xr-project.eu](https://iv4xr-project.eu/).

Status: Prototype / early development

## Introduction

Space Engineers is a sandbox game by Keen Software House. This project is a plugin for the game which enables its integration with the iv4XR testing framework. The plugin runs a TCP/IP server with JSON-based API. It allows to access surrounding of the player's character in a structured form (a World Object Model, WOM) and to control the character. Those are the two defining features, more will be added during the development.

## How to build

Requires Space Engineers codebase (which is not open) to compile. The resulting plug-in (a couple of .NET libraries), however, works with the official Steam version of Space Engineers without any modification of the game.

## How to run the game with this plugin

1. Obtain the binary release of Space Engineers (buy it on Steam or get a key). Install the game.
2. Put the plugin libraries into the folder with the game binaries. A common location is `C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64`. It's recommended to create a subfolder there (such as `iv4xr-debug`) and put the libraries inside the subfolder. (Alternatively, it can be a symbolic link to the build folder of the plugin project.)
3. Right-click on the game title in the Steam library list and open its properties window. Click on the **Set launch options...** button. Add the option `-plugin` and list the libraries. The result should be something like this: `-plugin "iv4xr-debug\Ivxr.PlugIndependentLib.dll" "iv4xr-debug\Ivxr.SePlugin.dll"`.
4. Run the game.
5. Start a scenario. (It's necessary to do it manually for now. Should be done automatically by the testing framework in the future.)
6. If the plugin works correctly, a TCP/IP server is listening for JSON-based commands on a fixed port number. (The current development version uses port number 9678.) 
   Another sign of life is a log file present in user's app data folder such as: `C:\Users\<username>\AppData\Roaming\SpaceEngineers\ivxr-plugin.log`

## API

The network protocol is just some proof of concept for now, so it's possible it will change. It is based on [the Lab Recruits demo](https://github.com/iv4xr-project/iv4xrDemo). The protocol is based on JSON commands split by newlines.

Currently implemented commands:

- **Observe** – basic version; returns list of entities and their location in the agent's surrounding.
- **MoveAndRotate** – allows to move and and rotate the agent in all directions.
- Disconnect

There's a Java project derived from the Lab Recruits demo that contains a demo client in the form of unit tests. The [repository is here](https://github.com/iv4xr-project/iv4xrDemo-space-engineers).

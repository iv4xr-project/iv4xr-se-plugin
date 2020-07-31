# iv4xr-se-plugin
Integration of Space Engineers to the iv4XR framework.

Status: Prototype / early development

## Introduction

This project is a Space Engineers (a game by Keen Software House) plugin which enables integration with the iv4XR testing framework. The plugin runs a TCP/IP server with JSON-based API. It allows to access surrounding of the player's character in a structured form (a World Object Model, WOM) and to control the character. Those are the two defining features, more will be added during the development.

## How to build

Requires Space Engineers codebase (which is not open) to compile. The resulting plug-in (a couple of .NET libraries), however, works with the official Steam version of Space Engineers without any modification of the game.

## How to run

1. Obtain Space Engineers (buy it on Steam or get a key) and download the game.

2. Put the plugin libraries into the folder with the game binaries. A common location might be `C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64`. It's recommended to create a subfolder there (such as `iv4xr-debug`) and put the libraries inside the subfolder. (Alternatively, it can be a symbolic link to the build folder of the plugin project.)

3. Right-click on the game title in the Steam library list and open its properties window. Click on the **Set launch options...** button. Add the option `-plugin` and list the libraries. The result should be something like this: `-plugin "iv4xr-debug\Ivxr.PlugIndependentLib.dll" "iv4xr-debug\Ivxr.SePlugin.dll"`.

TODO

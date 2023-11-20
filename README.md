[<img src="./JvmClient/docs/iv4xr_logo_1200dpi.png" alt="iv4XR logo" align="right" width="15%">](https://iv4xr-project.eu/)

# iv4XR Space Engineers Plugin

A plugin providing integration of [Space Engineers](https://www.spaceengineersgame.com/) to the [iv4XR framework](https://iv4xr-project.eu/).

The plugin is developed by [GoodAI](https://www.goodai.com/).
[<img src="./JvmClient/docs/goodai-logo.png" alt="GoodAI logo" align="right" width="15%">](https://www.goodai.com/)

## Introduction

Space Engineers is a sandbox game by [Keen Software House](https://www.keenswh.com/).

<img src="./JvmClient/docs/SE-sotf1.png" alt="Space Engineers" width="100%">

The plugin exposes the game structured data, functionality and allows remote control.

## (OLD game version 202) How to run the game with plugin until game version 202

1. Clone this repository or download as zip.
2. Install the plugin and 3rd party libraries by running [install.bat](Scripts/install.bat) file in the Scripts directory.
3. Run the game by running [start.bat](Scripts/start.bat) file in the Scripts directory. You should see Steam popup `Allow game launch?`
4. To make this permanent from Steam:
    - Right-click on the game title in the Steam library list and open its `Properties`.
    - Add the option `-plugin Ivxr.SePlugin.dll` to [launch options](https://help.steampowered.com/en/faqs/view/7d01-d2dd-d75e-2955).
5. Use [Kotlin](JvmClient/README.md) or [Python](PythonClient/README.md) client to communicate with the game.

To get more detailed information about these steps, check [manual installation](Docs/manual-installation.md), you can also [build manually](Docs/how-to-build.md).

## Game version 203

Keen Software House has removed the usage of the `-plugin` launcher parameter to protect players' systems for security reasons: [SpaceEngineers plugins](https://www.spaceengineersgame.com/plugins/)

They have partnered with the community to provide an open-source plugin solution via GitHub: [PluginLoader GitHub](https://github.com/sepluginloader/PluginLoader)

### How to run the game with the plugin for the game version 203

1. If you have been using the plugin with previous versions:
    - Delete all possible Ivxr DLLs files (`Ivxr.SePlugin & Ivxr.SpaceEngineers & Ivxr.PlugIndependentLib`) 
    - Delete all DLLs dependencies (`ImpromptuInterface & AustinHarris.JsonRpc & Newtonsoft.Json`) 
    - That exist in the SpaceEngineers game directory: `C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64`
2. Create two new `\Plugins\Local` folders in the SpaceEngineers game directory:  
    - `C:\Program Files (x86)\Steam\steamapps\common\SpaceEngineers\Bin64\Plugins\Local`
3. In the new `\Plugins\Local` folder:
    - Extract the Ivxr DLLs files (`Ivxr.SePlugin & Ivxr.SpaceEngineers & Ivxr.PlugIndependentLib`) 
and DLLs dependencies (`ImpromptuInterface & AustinHarris.JsonRpc & Newtonsoft.Json`)
    - You can use these compiled DLLs if needed: [SpaceEngineers_iv4xr_0.9.0_game_v203.zip](https://github.com/iv4xr-project/TESTAR_iv4xr/releases/download/v3.6/SpaceEngineers_iv4xr_0.9.0_game_v203.zip)
4. Follow [PluginLoader SpaceEngineersLauncher](https://github.com/sepluginloader/SpaceEngineersLauncher) instructions to install SpaceEngineers game with the new Plugin Loader.
5. The new launcher you will need to use is the executable: `SpaceEngineersLauncher.exe`
6. If Plugin Loader is downloaded and launched correctly, a new `Plugins` button will appear in the SpaceEngineers game main GUI:
    - Click `Plugins` button --> Click `Add button` icon --> Scroll down and Click `Add local file` button.
	- Type and select the `Ivxr.SePlugin`
	- Apply changes. Then the SpaceEngineers game will restart with the plugin running.

`INFO:` If you see error pop-ups, allowing you to open a log file with DLL exceptions, I recommend doing a clean install of the SpaceEngineers v203 game. This could be due to the Plugin Loaders working with different DLLs function dependencies in the `\SpaceEngineers\Bin64` folder.

## Troubleshooting

- Check Space Engineers logs first at `~\AppData\Roaming\SpaceEngineers`.

- Make sure Windows is OK to run the libraries. **Windows (10+) blocks "randomly" downloaded libraries.** Typical error in log is: `System.NotSupportedException`: *An attempt was
  made to load an assembly from a network location...*. To fix them:
  - Right-click each of them and open file `properties`. 
  - Look for Security section on the bottom part of the `General` tab. 
  - You might see a message: `This file came from another computer and might be blocked...`. If so, check
    the `Unblock` checkbox.

- If the plugin works correctly, a TCP/IP server is listening for JSON-RPC calls on the port `3333`. (See the config file in
  user's AppData folder `~\AppData\Roaming\SpaceEngineers\ivxr-plugin.config` for the current port number). You can safely delete this file if you think it's done incorrectly.

- Another sign of life is the log file `ivxr-plugin_xxxxx.log` present in the same folder.


## Game mechanics and functionality

This section links to specific, more detailed parts of the documentation which describe functionality relevant for the plugin:

- [Basic information](JvmClient/docs/Basics.MD) about Space Engineers engine for purposes of using the plugin.
- Information about [blocks](JvmClient/docs/Blocks.MD).
- Information about character/vehicle [movement](JvmClient/docs/Movement.MD).
- Controlling the game using [screens](JvmClient/docs/Screens.MD).
- Using the plugin in [multiplayer](JvmClient/docs/Multiplayer.MD).
- [Block ownership](JvmClient/docs/Ownership.MD).
- [Multiple characters](JvmClient/docs/Multiple-Characters.MD) in a single game.
- Using the plugin for [automated testing](JvmClient/docs/Automated-Testing.MD).


## Other links

- [Naming convention](Docs/naming-convention.md)
- [How to build](Docs/how-to-build.md)
- [Manual installation](Docs/manual-installation.md)
- [How to make a release](Docs/release-howto.md)
- [Protocol and API](Docs/protocol-api.md)

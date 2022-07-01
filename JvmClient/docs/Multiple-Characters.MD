# Multiple characters

## Overview

Most of the plugin commands by default control the main local character, which is also bound to the camera and input
controls. It is possible to add more characters into the game by using Admin.Character.Create. To switch between
characters, use Admin.Character.Switch, passing character ID. Main character ID is always se0.

A few points:

- This feature is a bit experimental, there may be glitches and unexpected behaviour, please
  file [issue](https://github.com/iv4xr-project/iv4xr-se-plugin/issues/new) if you run into anything.
- Toolbar is shared between characters.
- Only the main character can place blocks normally (but any character can place blocks through admin commands).
- The behaviour is overall glitchy and buggy, we are considering removing it or reimplementing only for single-player,
  because single-player has some built-in support for this.
- If you need serious multiplayer, just to the real [multiplayer](Multiplayer.MD).

## Commands unaffected by character switch

- Session.LoadScenario
- Observer.TakeScreenshot
- Blocks.Place (always places for the main character - use Admin.Blocks.PlaceInGrid/PlaceAt to place blocks for other
  characters)

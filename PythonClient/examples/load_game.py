"""
This example goes to "Load game" screen (game has to be in the main menu) and then double-clicks the first item from
the list.
"""
import time

from spaceengineers.proxy import SpaceEngineersProxy

if __name__ == "__main__":
    se = SpaceEngineersProxy.localhost()
    se.Screens.MainMenu.LoadGame()
    time.sleep(1)
    se.Screens.LoadGame.DoubleClickWorld(index=0)

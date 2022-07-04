import time

from spaceengineers.proxy import SpaceEngineersProxy

if __name__ == '__main__':
    se = SpaceEngineersProxy.localhost()
    se.Screens.MainMenu.LoadGame()
    time.sleep(1)
    se.Screens.LoadGame.DoubleClickWorld(index=0)

from spaceengineers.proxy import SpaceEngineersProxy

if __name__ == '__main__':
    se = SpaceEngineersProxy.localhost()
    print(se.Observer.Observe())

from spaceengineers.models import Vec3F, Vec2F
from spaceengineers.proxy import SpaceEngineersProxy

if __name__ == "__main__":
    se = SpaceEngineersProxy.localhost()
    se.character.moveAndRotate(
        movement=Vec3F(Z=-1, X=0, Y=0), ticks=60, roll=0, rotation3=Vec2F(X=0, Y=0)
    )

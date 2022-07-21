# -*- coding: utf-8 -*-

from spaceengineers.models import Vec3F, DefinitionId, Vec3I
from spaceengineers.proxy import SpaceEngineersProxy


def generate_image(url):
    LARGE_BLOCK_CUBE_SIDE_SIZE = 2.5
    from PIL import Image

    img = Image.open(url)
    rgb_image = img.load()
    width, height = img.size

    se = SpaceEngineersProxy.localhost()
    se.Admin.Character.Teleport(
        position=Vec3F(X=3, Y=3, Z=3),
        orientationForward=Vec3I(0, 0, 1),
        orientationUp=Vec3I(0, 1, 0)
    )
    definitionId = DefinitionId(Id="MyObjectBuilder_CubeBlock", Type="LargeHeavyBlockArmorBlock")
    gridId = None
    z = 0
    for x in range(0, width):
        for y in range(0, height):
            (r, g, b, a) = rgb_image[x, y]
            color = Vec3F(r / 255, g / 255, b / 255)
            if not gridId:
                se.Admin.Blocks.PlaceAt(
                    blockDefinitionId=definitionId,
                    position=Vec3F(
                        X=x * LARGE_BLOCK_CUBE_SIDE_SIZE,
                        Y=y * LARGE_BLOCK_CUBE_SIDE_SIZE,
                        Z=z * LARGE_BLOCK_CUBE_SIDE_SIZE
                    ),
                    orientationUp=Vec3F(X=0, Y=1, Z=0),
                    orientationForward=Vec3F(X=0, Y=0, Z=-1),
                    color=color,
                )
                gridId = se.Observer.ObserveNewBlocks().Grids[0]["Id"]
                se.Admin.Character.Teleport(
                    position=Vec3F(
                        X=width * LARGE_BLOCK_CUBE_SIDE_SIZE / 2,
                        Y=height * LARGE_BLOCK_CUBE_SIDE_SIZE / 2,
                        Z=-60
                    ),
                    orientationForward=Vec3I(0, 0, 1),
                    orientationUp=Vec3I(0, -1, 0)
                )
            elif gridId:
                place_in_grid(definitionId, gridId, se, x, y, z, color=color)


def place_in_grid(definitionId, gridId, se, x, y, z, color):
    se.Admin.Blocks.PlaceInGrid(
        gridId=gridId,
        blockDefinitionId=definitionId,
        minPosition=Vec3I(X=x, Y=y, Z=z),
        orientationUp=Vec3I(X=0, Y=1, Z=0),
        orientationForward=Vec3I(X=0, Y=0, Z=-1),
        color=color,
    )


if __name__ == '__main__':
    generate_image("../../resources/goodai-logo64.png")

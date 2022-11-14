# -*- coding: utf-8 -*-

from mazelib import Maze
from mazelib.generate.Prims import Prims

from spaceengineers.models import Vec3F, DefinitionId, Vec3I
from spaceengineers.proxy import SpaceEngineersProxy


def print_text(text):
    LARGE_BLOCK_CUBE_SIDE_SIZE = 2.5
    lines = text.splitlines()
    height = len(lines) + 2
    width = len(max(lines, key=lambda x: len(x))) + 2

    se = SpaceEngineersProxy.localhost()
    se.Admin.Character.Teleport(position=Vec3F(X=10, Y=10, Z=10))
    definitionId = DefinitionId(Id="MyObjectBuilder_CubeBlock", Type="LargeHeavyBlockArmorBlock")
    gridId = None
    z = 0
    background_color = Vec3F(1, 1, 1)
    for x in range(0, width):
        for y in range(0, height):
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
                    color=background_color,
                )
                gridId = se.Observer.ObserveNewBlocks().Grids[0]["Id"]
                se.Admin.Character.Teleport(
                    position=Vec3F(
                        X=width * LARGE_BLOCK_CUBE_SIDE_SIZE / 2,
                        Y=height * LARGE_BLOCK_CUBE_SIDE_SIZE / 2,
                        Z=-30
                    ),
                    orientationForward=Vec3I(0, 0, 1),
                    orientationUp=Vec3I(0, -1, 0)
                )
            else:
                place_in_grid(definitionId, gridId, se, x, y, z, color=background_color)

    z = -1
    y = 1
    for line in lines:
        x = 1
        for letter in line:
            if letter != " ":
                place_in_grid(letter_to_block(letter), gridId, se, x, y, z, Vec3F(0, 0, 0))
                #place_in_grid(definitionId, gridId, se, x, y, z, Vec3F(0.5, 1, 1))
            x += 1
        y += 1


def letter_to_block(letter) -> DefinitionId:
    return DefinitionId(Id="MyObjectBuilder_CubeBlock", Type=f"LargeSymbol{letter.upper()}")


def place_in_grid(definitionId, gridId, se, x, y, z, color=None):
    se.Admin.Blocks.PlaceInGrid(
        gridId=gridId,
        blockDefinitionId=definitionId,
        minPosition=Vec3I(X=x, Y=y, Z=z),
        orientationUp=Vec3I(X=0, Y=0, Z=-1),
        orientationForward=Vec3I(X=0, Y=-1, Z=0),
        color=color,
    )


if __name__ == '__main__':
    print_text("""
Hello world

from

Space Engineers
""".strip())

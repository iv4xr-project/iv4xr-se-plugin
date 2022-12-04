# -*- coding: utf-8 -*-

from mazelib import Maze
from mazelib.generate.Prims import Prims

from spaceengineers.models import Vec3F, DefinitionId, Vec3I
from spaceengineers.proxy import SpaceEngineersProxy


def generate_maze(width, height):
    LARGE_BLOCK_CUBE_SIDE_SIZE = 2.5

    maze = Maze()
    maze.generator = Prims(width, height)
    maze.generate()
    se = SpaceEngineersProxy.localhost()
    se.Admin.Character.Teleport(position=Vec3F(X=10, Y=10, Z=10))
    definitionId = DefinitionId(
        Id="MyObjectBuilder_CubeBlock", Type="LargeHeavyBlockArmorBlock"
    )
    gridId = None
    z = 0
    for x in range(0, width):
        for y in range(0, height):
            if not gridId:
                se.Admin.Blocks.PlaceAt(
                    blockDefinitionId=definitionId,
                    position=Vec3F(
                        X=x * LARGE_BLOCK_CUBE_SIDE_SIZE,
                        Y=y * LARGE_BLOCK_CUBE_SIDE_SIZE,
                        Z=z * LARGE_BLOCK_CUBE_SIDE_SIZE,
                    ),
                    orientationUp=Vec3F(X=0, Y=1, Z=0),
                    orientationForward=Vec3F(X=0, Y=0, Z=-1),
                    color=Vec3F(0.5, 1, 1),
                )
                gridId = se.Observer.ObserveNewBlocks().Grids[0]["Id"]
                se.Admin.Character.Teleport(
                    position=Vec3F(
                        X=width * LARGE_BLOCK_CUBE_SIDE_SIZE / 2,
                        Y=height * LARGE_BLOCK_CUBE_SIDE_SIZE / 2,
                        Z=-60,
                    ),
                    orientationForward=Vec3I(0, 0, 1),
                    orientationUp=Vec3I(0, -1, 0),
                )
            else:
                place_in_grid(definitionId, gridId, se, x, y, z)
    z = -1
    for x in range(0, width):
        for y in range(0, height):
            if maze.grid[x][y]:
                place_in_grid(definitionId, gridId, se, x, y, z)
                place_in_grid(definitionId, gridId, se, x, y, z - 1)


def place_in_grid(definitionId, gridId, se, x, y, z):
    se.Admin.Blocks.PlaceInGrid(
        gridId=gridId,
        blockDefinitionId=definitionId,
        minPosition=Vec3I(X=x, Y=y, Z=z),
        orientationUp=Vec3I(X=0, Y=1, Z=0),
        orientationForward=Vec3I(X=0, Y=0, Z=-1),
        color=Vec3F(0.5, 1, 1),
    )


if __name__ == "__main__":
    generate_maze(38, 30)

package spaceEngineers.util.generator.maze

import kotlin.random.Random


class Maze(val width: Int, val height: Int, val startX: Int = 2, val rnd: Random = Random.Default) {

    enum class Cell(
        val canMove: Boolean,
    ) {
        WALL(false), SPACE(true), START(true), END(true), OUT_OF_BOUNDS(false)
    }

    private val data = Array(width) { i ->
        Array(height) { j -> Cell.WALL }
    }

    init {
        generate()
    }

    operator fun get(x: Int, y: Int): Cell {
        if (x < 0 || y < 0 || y > height - 1 || x > width - 1) {
            return Cell.OUT_OF_BOUNDS
        }
        return data[x][y]
    }

    val start = Position(startX, 1)
    val end = Position(width - 3, height - 2)

    private fun carve(x: Int, y: Int) {

        val upx = intArrayOf(1, -1, 0, 0)
        val upy = intArrayOf(0, 0, 1, -1)

        var dir = rnd.nextInt(4)
        var count = 0
        while (count < 4) {
            val x1 = x + upx[dir]
            val y1 = y + upy[dir]
            val x2 = x1 + upx[dir]
            val y2 = y1 + upy[dir]
            if (data[x1][y1] == Cell.WALL && data[x2][y2] == Cell.WALL) {
                data[x1][y1] = Cell.SPACE
                data[x2][y2] = Cell.SPACE
                carve(x2, y2)
            } else {
                dir = (dir + 1) % 4
                count += 1
            }
        }

    }

    private fun generate(): Array<Array<Cell>> {

        for (x in 0 until width) {
            for (y in 0 until height) {
                data[x][y] = Cell.WALL
            }
        }

        for (x in 0 until width) {
            data[x][0] = Cell.SPACE
            data[x][height - 1] = Cell.SPACE
        }
        for (y in 0 until height) {
            data[0][y] = Cell.SPACE
            data[width - 1][y] = Cell.SPACE
        }

        data[startX][2] = Cell.SPACE
        carve(startX, 2)

        data[startX][1] = Cell.START
        data[width - 3][height - 2] = Cell.END

        for (x in 0 until width) {
            data[x][0] = Cell.WALL
            data[x][height - 1] = Cell.WALL
        }
        for (y in 0 until height) {
            data[0][y] = Cell.WALL
            data[width - 1][y] = Cell.WALL
        }
        return data
    }

    override fun toString(): String {
        var result = "";
        for (y in 0 until height) {
            for (x in 0 until width) {
                when (data[x][y]) {
                    Cell.END ->
                        result += ("ee")
                    Cell.SPACE ->
                        result += ("  ")
                    Cell.WALL ->
                        result += ("██")
                    Cell.START ->
                        result += ("ss")
                    Cell.OUT_OF_BOUNDS ->
                        result += ("??")
                }
            }
            result += "\n"
        }
        return result
    }
}

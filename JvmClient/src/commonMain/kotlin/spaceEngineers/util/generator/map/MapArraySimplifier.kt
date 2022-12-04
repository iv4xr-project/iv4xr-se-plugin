package spaceEngineers.util.generator.map

inline fun <reified T : Any?> Array<Array<T>>.simplifiedArray(): Array<Array<T>> {
    return simplifyColumns().simplifyRows()
}

inline fun <reified T : Any?> Array<Array<T>>.simplifyColumns(): Array<Array<T>> {
    val uc = uniqueColumns()
    return Array(uc.size) { x ->
        Array(height) { y ->
            this[uc[x]][y]
        }
    }
}

inline fun <reified T : Any?> Array<Array<T>>.simplifyRows(): Array<Array<T>> {
    val ur = uniqueRows()
    return Array(width) { x ->
        Array(ur.size) { y ->
            this[x][ur[y]]
        }
    }
}

fun <T : Any?> Array<Array<T>>.uniqueColumns(): List<Int> {
    val uniqueColumns = mutableListOf<Int>()
    var x = 0
    while (x < width) {
        uniqueColumns.add(x)
        val column = column(x)
        val sameColumns = countIdenticalColumns(x, column)
        x += if (sameColumns > 0) {
            sameColumns
        } else {
            1
        }
    }
    return uniqueColumns
}

inline fun <reified T : Any?> Array<Array<T>>.uniqueRows(): List<Int> {
    val uniqueRows = mutableListOf<Int>()
    var y = 0
    while (y < height) {
        uniqueRows.add(y)
        val row = row(y)
        val sameRows = countIdenticalRows(y, row)
        y += if (sameRows > 0) {
            sameRows
        } else {
            1
        }
    }
    return uniqueRows
}

inline fun <reified T : Any?> Array<Array<T>>.countIdenticalRows(
    rowIndex: Int,
    row: Array<T>
): Int {
    val sameColumns = (rowIndex + 1 until height).indexOfFirst { yOffset ->
        !(row(yOffset) contentEquals row)
    }.takeIf { it >= 0 } ?: (height - rowIndex)
    return sameColumns + 1
}

val <T : Any?> Array<Array<T>>.width
    get() = size

val <T : Any?> Array<Array<T>>.height
    get() = firstOrNull()?.size ?: 0

fun <T : Any?> Array<Array<T>>.countIdenticalColumns(
    columnIndex: Int,
    column: Array<T>
): Int {
    val sameColumns = (columnIndex + 1 until width).indexOfFirst { xOffset ->
        !(column(xOffset) contentEquals column)
    }.takeIf { it >= 0 } ?: (width - columnIndex)
    return sameColumns + 1
}

inline fun <reified T : Any?> Array<Array<T>>.row(y: Int): Array<T> {
    return (0 until width).map { x ->
        this[x][y]
    }.toTypedArray()
}

fun <T : Any?> Array<Array<T>>.column(x: Int): Array<T> {
    return this[x]
}

fun main() {
    fun part1(grid: List<String>): Int {
        val width = grid.first().length
        val height = grid.size

        val partNumberCoordinates = mutableSetOf<Pair<Int, Int>>()
        grid.forEachIndexed { r, row ->
            row.forEachIndexed { c, char ->
                if (char !in '0'..'9' && char != '.') {
                    moves.forEach { (dX, dY) ->
                        val x = clamp(c + dX, 0, width - 1)
                        val y = clamp(r + dY, 0, height - 1)
                        if (grid[y][x] in '0'..'9') {
                            var t = x
                            while (t - 1 >= 0 && grid[y][t - 1] in '0'..'9') {
                                t--
                            }
                            partNumberCoordinates.add(y to t)
                        }
                    }
                }
            }
        }
        val partNumbers = partNumberCoordinates.map { (r, c) ->
            var t = c
            while (t < width && grid[r][t] in '0'..'9') {
                t++
            }
            grid[r].substring(c, t).toInt()
        }
        return partNumbers.sum()
    }

    fun part2(grid: List<String>): Int {
        val width = grid.first().length
        val height = grid.size
        var sum = 0
        grid.forEachIndexed { r, row ->
            row.forEachIndexed { c, char ->
                if (char == '*') {
                    val adjacentPartNumbers = mutableListOf<Int>()
                    val visited = mutableSetOf<Pair<Int, Int>>()
                    moves.forEach { (dX, dY) ->
                        val x = clamp(c + dX, 0, width - 1)
                        val y = clamp(r + dY, 0, height - 1)
                        if (y to x in visited || grid[y][x] !in '0'..'9') return@forEach
                        visited.add(y to x)
                        var t = x
                        while (t - 1 >= 0 && grid[y][t - 1] in '0'..'9') {
                            t--
                            visited.add(y to t)
                        }
                        val numberStart = t
                        while (t < width && grid[y][t] in '0'..'9') {
                            t++
                            visited.add(y to t)
                        }
                        val numberString = grid[y].substring(numberStart, t)
                        if (numberString.isNotBlank()) {
                            adjacentPartNumbers.add(numberString.toInt())
                        }
                    }
                    if (adjacentPartNumbers.size == 2) {
                        sum += adjacentPartNumbers.run { first() * last() }
                    }
                }
            }
        }
        return sum
    }

    val testInput = readInput("Day03_test")
    part1(testInput).println()
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

val moves = listOf(
        -1 to -1,
        0 to -1,
        1 to -1,
        -1 to 0,
        1 to 0,
        -1 to 1,
        0 to 1,
        1 to 1,
)

fun clamp(x: Int, min: Int, max: Int): Int =
        if (x < min) min
        else if (x > max) max
        else x
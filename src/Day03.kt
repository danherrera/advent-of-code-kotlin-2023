fun main() = Day3().run()

class Day3 : Challenge<Int>(3) {
    override val testResults = ExpectedTestInputResults(
        part1 = 4361,
        part2 = 467835,
    )

    override fun part1(grid: List<String>): Int {
        val width = grid.first().length
        val height = grid.size

        val partNumberCoordinates = mutableSetOf<Pair<Int, Int>>()
        for ((r, row) in grid.withIndex()) {
            for ((c, char) in row.withIndex()) {
                if (char !in '0'..'9' && char != '.') {
                    for ((dY, dX) in moves) {
                        val y = clamp(r + dY, 0, height - 1)
                        val x = clamp(c + dX, 0, width - 1)

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

    override fun part2(grid: List<String>): Int {
        val width = grid.first().length
        val height = grid.size
        var sum = 0
        for ((r, row) in grid.withIndex()) {
            for ((c, char) in row.withIndex()) {
                if (char == '*') {
                    val adjacentPartNumbers = mutableListOf<Int>()
                    val visited = mutableSetOf<Pair<Int, Int>>()
                    for ((dY, dX) in moves) {
                        val y = clamp(r + dY, 0, height - 1)
                        val x = clamp(c + dX, 0, width - 1)
                        if (y to x in visited || grid[y][x] !in '0'..'9') continue
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

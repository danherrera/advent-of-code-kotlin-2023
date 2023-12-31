fun main() = Day11().run()
class Day11 : Challenge<Long>(11) {
    override val testResults = ExpectedTestInputResults<Long>(
            374L,
            82000210L,
    )

    override fun part1(input: List<String>): Long {
        val universe = Universe.from(input)
        return universe.galaxyPairs.map { galaxyPair ->
            galaxyPair.distanceWithExpandedUniverse(
                    universe.rowsWithoutGalaxies,
                    universe.columnsWithoutGalaxies,
            )
        }.sum()
    }

    override fun part2(input: List<String>): Long {
        val universe = Universe.from(input)
        return universe.galaxyPairs.map { galaxyPair ->
            galaxyPair.distanceWithExpandedUniverse(
                    universe.rowsWithoutGalaxies,
                    universe.columnsWithoutGalaxies,
                    1_000_000
            )
        }.sum()
    }

}

data class Dimensions(val rows: Long, val columns: Long)

private class Universe(
        val galaxyCoordinates: List<Pair<Int, Int>>,
        val rowsWithoutGalaxies: List<Int>,
        val columnsWithoutGalaxies: List<Int>,
        val galaxyPairs: Set<GalaxyPair>,
) {
    companion object {
        fun from(input: List<String>): Universe {
            val rowsWithoutGalaxies = MutableList<Int>(input.size) { it }
            val columnsWithoutGalaxies = MutableList<Int>(input.first().length) { it }
            return input.flatMapIndexed { r, row ->
                row.mapIndexedNotNull { c, column ->
                    when (column) {
                        '#' -> {
                            rowsWithoutGalaxies.remove(r)
                            columnsWithoutGalaxies.remove(c)
                            r to c
                        }

                        else -> null
                    }
                }
            }.let { galaxyCoordinates ->
                Universe(
                        galaxyCoordinates,
                        rowsWithoutGalaxies,
                        columnsWithoutGalaxies,
                        galaxyCoordinates.flatMap { a ->
                            galaxyCoordinates.mapNotNull { b ->
                                GalaxyPair.from(a, b)
                            }
                        }.toSet()
                )
            }
        }
    }
}

private data class GalaxyPair(
        val from: Pair<Int, Int>,
        val to: Pair<Int, Int>,
) {
    companion object {
        fun from(a: Pair<Int, Int>, b: Pair<Int, Int>): GalaxyPair? {
            return when {
                a.first < b.first -> a to b
                b.first < a.first -> b to a
                a.second < b.second -> a to b
                b.second < a.second -> b to a
                else -> null // same coordinate
            }?.let { (from, to) ->
                GalaxyPair(from, to)
            }
        }
    }

    val distance: Int = Math.abs(to.first - from.first) + Math.abs(to.second - from.second)

    fun distanceWithExpandedUniverse(expandedRows: List<Int>, expandedColumns: List<Int>, multiplier: Long = 2): Long {
        return distance +
                expandedRows.filter { it in minOf(from.first, to.first) until maxOf(from.first, to.first) }.size * (multiplier - 1) +
                expandedColumns.filter { it in minOf(from.second, to.second) until maxOf(from.second, to.second) }.size * (multiplier - 1)
    }

    fun minDistanceWithExpandedUniverse(expandedRows: List<Int>, expandedColumns: List<Int>, multiplier: Long = 1): Long {
        // x1,y1 to x2,y2
        // 0-y1;y2-maxY
        // y1-y2
        return distance +
                expandedRows.filter { it in minOf(from.first, to.first) until maxOf(from.first, to.first) }.size * multiplier + expandedColumns.filter { it in minOf(from.second, to.second) until maxOf(from.second, to.second) }.size * multiplier
    }
}
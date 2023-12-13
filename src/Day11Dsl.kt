fun main() = day(11) {
    part(1) { input ->
        expect(sample = 374L)
        val universe = Universe2.from(input)
        universe.galaxyPairs.sumOf { galaxyPair ->
            galaxyPair.distanceWithExpandedUniverse(
                    universe.rowsWithoutGalaxies,
                    universe.columnsWithoutGalaxies,
            )
        }
    }

    part(2) { input ->
        val universe = Universe2.from(input)
        expect(sample = 82000210L)
        universe.galaxyPairs.sumOf { galaxyPair ->
            galaxyPair.distanceWithExpandedUniverse(
                    universe.rowsWithoutGalaxies,
                    universe.columnsWithoutGalaxies,
                    1_000_000
            )
        }
    }
}

private class Universe2(
        val galaxyCoordinates: List<Pair<Int, Int>>,
        val rowsWithoutGalaxies: List<Int>,
        val columnsWithoutGalaxies: List<Int>,
        val galaxyPairs: Set<GalaxyPair2>,
) {
    companion object {
        fun from(input: List<String>): Universe2 {
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
                Universe2(
                        galaxyCoordinates,
                        rowsWithoutGalaxies,
                        columnsWithoutGalaxies,
                        galaxyCoordinates.flatMap { a ->
                            galaxyCoordinates.mapNotNull { b ->
                                GalaxyPair2.from(a, b)
                            }
                        }.toSet()
                )
            }
        }
    }
}

private data class GalaxyPair2(
        val from: Pair<Int, Int>,
        val to: Pair<Int, Int>,
) {
    companion object {
        fun from(a: Pair<Int, Int>, b: Pair<Int, Int>): GalaxyPair2? {
            return when {
                a.first < b.first -> a to b
                b.first < a.first -> b to a
                a.second < b.second -> a to b
                b.second < a.second -> b to a
                else -> null // same coordinate
            }?.let { (from, to) ->
                GalaxyPair2(from, to)
            }
        }
    }

    val distance: Int = Math.abs(to.first - from.first) + Math.abs(to.second - from.second)

    fun distanceWithExpandedUniverse(expandedRows: List<Int>, expandedColumns: List<Int>, multiplier: Long = 2): Long {
        return distance +
                expandedRows.filter { it in minOf(from.first, to.first) until maxOf(from.first, to.first) }.size * (multiplier - 1) +
                expandedColumns.filter { it in minOf(from.second, to.second) until maxOf(from.second, to.second) }.size * (multiplier - 1)
    }
}

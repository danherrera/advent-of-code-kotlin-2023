fun main() = Day5().run()

typealias RangeMap = Map<LongRange, LongRange>
typealias MutableRangeMap = MutableMap<LongRange, LongRange>
typealias RangeEntry = Pair<LongRange, LongRange>

private fun createRangeEntry(targetStart: Long, sourceStart: Long, length: Long): RangeEntry {
    return (sourceStart until (sourceStart + length)) to (targetStart until (targetStart + length))
}

private fun findValue(x: Long, map: Map<LongRange, LongRange>): Long {
    val sourceRange = map.keys.firstOrNull { x in it } ?: return x
    val diff = x - sourceRange.min()
    val targetRange = map[sourceRange]!!
    return targetRange.min() + diff
}

class Day5 : Challenge<Long>(5) {
    override val testResults = ExpectedTestInputResults(
        part1 = 35L,
        part2 = 46L,
    )

    override fun part1(input: List<String>): Long {
        val almanac = Almanac.from(input)
        var min = Long.MAX_VALUE

        for (seed in almanac.seeds) {
            almanac.maps.fold(seed) { acc, map ->
                findValue(acc, map)
            }.let {
                min = minOf(min, it)
            }
        }
        return min
    }

    override fun part2(input: List<String>): Long {
        val almanac = Almanac.from(input)
        var min = Long.MAX_VALUE
        val ranges = almanac.seeds.chunked(2)
            .map {
                assert(it.size == 2)
                it.first() until (it.first() + it.last())
            }

        for ((r, range) in ranges.withIndex()) {
            "Range $r/${ranges.size}".println()
            for ((s, seed) in range.withIndex()) {
                "Seed $s/${range.last - range.first}".println()
                almanac.maps.fold(seed) { acc, map ->
                    findValue(acc, map)
                }.let {
                    min = minOf(min, it)
                }
            }
        }
        return min
    }
}

data class Almanac(
    val seeds: List<Long>,
    val maps: List<RangeMap>,
) {
    companion object {

        fun from(input: List<String>): Almanac {
            val seeds = input.first().run {
                substring(indexOf(' ') + 1).split(" ").map { it.toLong() }
            }

            val maps = mutableListOf<MutableRangeMap>()

            var lineIndex = 3
            var mapIndex = 0

            while (lineIndex < input.size) {
                while (lineIndex < input.size && input[lineIndex].isNotBlank()) {
                    if (mapIndex == maps.size) maps.add(mutableMapOf())
                    maps[mapIndex] += input[lineIndex].split(" ")
                        .map { it.toLong() }
                        .let { createRangeEntry(it[0], it[1], it[2]) }
                    lineIndex++
                }
                maps[mapIndex].println()
                mapIndex++
                lineIndex += 2
            }

            return Almanac(
                seeds = seeds,
                maps = maps,
            )
        }
    }
}

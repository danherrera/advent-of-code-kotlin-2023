fun main() = Day5().run()

class Day5 : Challenge<Long>(5) {
    override val testResults = ExpectedTestInputResults(
        part1 = 35L,
        part2 = 46L,
    )

    override fun part1(input: List<String>): Long {
        val almanac = AlmanacPart1.from(input)
        return almanac.seeds.map { seed ->
            almanac.categoryMappings.fold(seed, ::categoryMappingsTraversal)
        }
            .min()
    }

    override fun part2(input: List<String>): Long {
        val almanac = AlmanacPart2.from(input)
        for (location in 0..Long.MAX_VALUE) {
            val potentialSeed = almanac.categoryMappings.fold(location, ::categoryMappingsTraversal)
            val hasSeedInLocation = almanac.seedRanges.any { potentialSeed in it }
            if (hasSeedInLocation) {
                return location
            }
        }
        return -1L
    }

}

private fun categoryMappingsTraversal(acc: Long, categoryMapping: CategoryMapping): Long {
    val sourceRange = categoryMapping.sourceToTarget.keys
        .firstOrNull { acc in it } ?: return acc
    val delta = acc - sourceRange.first
    val targetRange = categoryMapping.sourceToTarget[sourceRange]!!
    return targetRange.first + delta
}

private data class AlmanacPart1(
    val seeds: List<Long>,
    val categoryMappings: List<CategoryMapping>,
) {
    companion object {
        fun from(input: List<String>): AlmanacPart1 {
            val seeds = input.first().substringAfter(": ").split(" ")
                .map { it.toLong() }

            val mappings = input.subList(2, input.size)
                .joinToString("\n")
                .split("\n\n")
                .map { it.split("\n").drop(1) }
                .map { rows ->
                    rows.associate { row ->
                        val rowData = row.split(" ")
                            .map { it.toLong() }
                        createRangeEntry(
                            rowData[0],
                            rowData[1],
                            rowData[2],
                        )
                    }.let(::CategoryMapping)
                }
            return AlmanacPart1(
                seeds,
                mappings,
            )
        }
    }
}

private data class AlmanacPart2(
    val seedRanges: List<LongRange>,
    val categoryMappings: List<CategoryMapping>,
) {
    companion object {
        fun from(input: List<String>): AlmanacPart2 {
            val seeds = input.first().substringAfter(": ").split(" ")
                .map { it.toLong() }
                .chunked(2)
                .map { (rangeStart, rangeLength) ->
                    rangeStart until (rangeStart + rangeLength)
                }

            val mappings = input.subList(2, input.size)
                .joinToString("\n")
                .split("\n\n")
                .map { it.split("\n").drop(1) }
                .map { rows ->
                    rows.associate { row ->
                        val rowData = row.split(" ")
                            .map { it.toLong() }
                        // reverse source and target to lookup seeds from location
                        createRangeEntry(
                            rowData[1],
                            rowData[0],
                            rowData[2],
                        )
                    }.let(::CategoryMapping)
                }
                .reversed() // reverse list to lookup starting with location
            return AlmanacPart2(
                seeds,
                mappings,
            )
        }
    }
}

private fun createRangeEntry(
    targetStart: Long,
    sourceStart: Long,
    rangeLength: Long,
): Pair<LongRange, LongRange> {
    val range = { start: Long ->
        start until (start + rangeLength)
    }
    return range(sourceStart) to range(targetStart)
}

// e.g. seed-to-soil, soil-to-fertilizer, etc.
private data class CategoryMapping(
    val sourceToTarget: Map<LongRange, LongRange>
)

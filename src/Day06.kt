fun main() = Day6().run()
class Day6 : Challenge<Long>(6) {
    override val testResults = ExpectedTestInputResults(
        part1 = 288L,
        part2 = 71503L,
    )

    override fun part1(input: List<String>): Long {
        return RacesDocument.fromPart1(input)
            .races
            .map {
                it.numberOfWaysToWin()
            }.reduce { acc, waysToWin ->
                acc * waysToWin
            }
    }

    override fun part2(input: List<String>): Long {
        return RacesDocument.fromPart2(input)
            .races
            .map { it.numberOfWaysToWin() }
            .first()
    }

}

fun Race.numberOfWaysToWin(): Long {
    var count = 0L
    for (t in 0 until durationInMilliseconds) {
        val speed = t
        val remainingTime = durationInMilliseconds - t
        val distanceToTravel = remainingTime * speed
        if (distanceToTravel > distanceInMillimeters) {
            count++
        }
    }
    return count
}

data class RacesDocument(
    val races: List<Race>,
) {
    companion object {
        fun fromPart1(input: List<String>): RacesDocument {
            val times = input.first().substringAfter(":").trim().split("\\s+".toRegex())
                .map { it.toLong() }
            val distances = input.last().substringAfter(":").trim().split("\\s+".toRegex())
                .map { it.toLong() }
            val races = times.mapIndexed { i, duration ->
                Race(
                    duration,
                    distances[i],
                )
            }
            return RacesDocument(races)
        }

        fun fromPart2(input: List<String>): RacesDocument {
            val duration = input.first().substringAfter(":").replace("\\s+".toRegex(), "").toLong()
            val distance = input.last().substringAfter(":").replace("\\s+".toRegex(), "").toLong()
            return RacesDocument(
                listOf(
                    Race(duration, distance)
                )
            )
        }
    }
}

data class Race(
    val durationInMilliseconds: Long,
    val distanceInMillimeters: Long,
)

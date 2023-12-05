fun main() = Day2().run()

class Day2 : Challenge<Int>(2) {
    override val testResults = ExpectedTestInputResults(
        part1 = 8,
        part2 = 2286,
    )

    override fun part1(input: List<String>): Int {
        return input
            .map { it.toGame() }
            .filter { game ->
                game.rounds.all { round ->
                    round.grabs.all { grab ->
                        when (grab) {
                            is Grab.Red -> grab.count <= 12
                            is Grab.Green -> grab.count <= 13
                            is Grab.Blue -> grab.count <= 14
                        }
                    }
                }
            }
            .sumOf { it.id }
    }

    override fun part2(input: List<String>): Int {
        return input
            .map { it.toGame() }
            .map { game ->
                var minimumRed = 0
                var minimumGreen = 0
                var minimumBlue = 0
                game.rounds.flatMap { it.grabs }.forEach { grab ->
                    when (grab) {
                        is Grab.Red -> minimumRed = maxOf(minimumRed, grab.count)
                        is Grab.Green -> minimumGreen = maxOf(minimumGreen, grab.count)
                        is Grab.Blue -> minimumBlue = maxOf(minimumBlue, grab.count)
                    }
                }
                minimumRed * minimumGreen * minimumBlue
            }
            .sum()
    }

}

private fun String.toGame(): Game {
    val id = substring(5, indexOf(':')).toInt()
    val rounds = substring(indexOf(": ") + 2).split("; ")
        .map { round ->
            Round(round.split(", ").map { grab ->
                Grab.from(grab)
            })
        }
    return Game(id, rounds)
}

private data class Game(
    val id: Int,
    val rounds: List<Round>,
)

private data class Round(
    val grabs: List<Grab>,
)

private sealed class Grab {
    data class Blue(val count: Int) : Grab()
    data class Red(val count: Int) : Grab()
    data class Green(val count: Int) : Grab()

    companion object {
        fun from(string: String): Grab {
            val components = string.split(" ")
            val count = components.first().toInt()
            val color = components.last()
            return when (color) {
                "blue" -> Blue(count)
                "red" -> Red(count)
                "green" -> Green(count)
                else -> throw IllegalArgumentException("'$string' does not conform to '# color'")
            }
        }
    }
}

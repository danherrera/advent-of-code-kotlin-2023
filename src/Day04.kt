import kotlin.math.pow

fun main() = Day4().run()

class Day4: Challenge<Int>(4) {
    override val testResults = ExpectedTestInputResults(
        part1 = 13,
        part2 = 30,
    )

    override fun part1(input: List<String>): Int {
        return input.map(Card::from).sumOf { it.calculatePoints() }
    }

    override fun part2(input: List<String>): Int {
        val count = Array(input.size) { 1 }
        val cards = input.map(Card::from)
        for ((i, card) in cards.withIndex()) {
            for (j in 1..card.numberOfMatching()) {
                count[i + j] += count[i]
            }
        }
        return count.sum()
    }
}

fun Card.numberOfMatching(): Int {
    return winningNumbers.intersect(yourNumbers).size
}

fun Card.calculatePoints(): Int {
    return 2.0.pow(numberOfMatching() - 1.0).toInt()
}

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val yourNumbers: List<Int>,
) {
    companion object {
        fun from(string: String): Card {
            return Card(
                id = string.substring(5, string.indexOf(":")).trim().toInt(),
                winningNumbers = with(string) {
                    substring(indexOf(":") + 1, indexOf("|")).toIntegerList()
                },
                yourNumbers = with(string) {
                    substring(indexOf("|") + 1).toIntegerList()
                }
            )
        }

        private fun String.toIntegerList(): List<Int> =
            trim().split("\\s+".toRegex()).map { it.toInt() }
    }
}

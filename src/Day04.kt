import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.map(Card::from).sumOf { it.calculatePoints() }
    }

    fun part2(input: List<String>): Int {
        val count = Array(input.size) { 1 }
        val cards = input.map(Card::from)
        for ((i, card) in cards.withIndex()) {
            for (j in 1..card.numberOfMatching()) {
                count[i + j] += count[i]
            }
        }
        return count.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
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
            trim().replace("\\s+".toRegex(), " ").split(" ").map { it.toInt() }
    }
}

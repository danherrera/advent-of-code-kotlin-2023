private val maxTextDigitCharacterLength = 5

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.filter { it.isDigit() }
        }.sumOf { "${it.first()}${it.last()}".toInt() }
    }

    fun part2(input: List<String>): Int {
        val stringToDigit = mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3,
                "four" to 4,
                "five" to 5,
                "six" to 6,
                "seven" to 7,
                "eight" to 8,
                "nine" to 9,
                "1" to 1,
                "2" to 2,
                "3" to 3,
                "4" to 4,
                "5" to 5,
                "6" to 6,
                "7" to 7,
                "8" to 8,
                "9" to 9,
        )

        fun String.findFirstDigit(): Int {
            (0..length).forEach { s ->
                val end = minOf(s + maxTextDigitCharacterLength, length)
                (s..end).forEach { e ->
                    stringToDigit[substring(s, e)]?.let {
                        return@findFirstDigit it
                    }
                }
            }
            return -1
        }

        fun String.findLastDigit(): Int {
            (length downTo 0).forEach { e ->
                val start = maxOf(e - maxTextDigitCharacterLength, 0)
                (e downTo start).forEach { s ->
                    stringToDigit[substring(s, e)]?.let {
                        return@findLastDigit it
                    }
                }
            }
            return -1
        }

        return input
            .sumOf { "${it.findFirstDigit()}${it.findLastDigit()}".toInt() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

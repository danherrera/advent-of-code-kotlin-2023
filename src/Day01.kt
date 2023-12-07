fun main() = Day1().run()

class Day1 : Challenge<Int>(1) {
    override val testResults = ExpectedTestInputResults(
        part1 = 142,
        part2 = 281,
    )

    override fun part1(input: List<String>): Int {
        return input.map { line ->
            line.filter { it.isDigit() }
        }.sumOf { "${it.first()}${it.last()}".toInt() }
    }

    override fun part2(input: List<String>): Int {

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

        val maxTextDigitCharacterLength = stringToDigit.keys.maxOfOrNull { it.length }!!

        fun String.findFirstDigit(): Int {
            for (s in 0..length) {
                for (e in s..minOf(s + maxTextDigitCharacterLength, length)) {
                    stringToDigit[substring(s, e)]?.let {
                        return it
                    }
                }
            }
            return -1
        }

        fun String.findLastDigit(): Int {
            for (e in length downTo 0) {
                for (s in e downTo maxOf(e - maxTextDigitCharacterLength, 0)) {
                    stringToDigit[substring(s, e)]?.let {
                        return it
                    }
                }
            }
            return -1
        }

        return input
            .sumOf { "${it.findFirstDigit()}${it.findLastDigit()}".toInt() }
    }
}

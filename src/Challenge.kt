import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe

abstract class Challenge<Output>(private val day: Int) {

    abstract val testResults: ExpectedTestInputResults<Output>

    abstract fun part1(input: List<String>): Output
    abstract fun part2(input: List<String>): Output

    fun run() {
        val dayString = day.toString().padStart(2, '0')

        val testInput = readInput("Day${dayString}_test")
        val testInput2 = runCatching { readInput("Day${dayString}_test2") }
                .getOrDefault(testInput)
                .takeIf { it.isNotEmpty() }
                ?: testInput

        val input = readInput("Day${dayString}")
        """
            =============
               Day $dayString
            =============
        """.trimIndent().println()
        val part1TestResult = part1(testInput)
        withClue("Part 1 test results should match expectation") {
            part1TestResult shouldBe testResults.part1
        }
        println("✅ Part 1 Test Input: $part1TestResult")
        "Part 1: ${part1(input)}".println()

        val part2TestResult = part2(testInput2)
        withClue("Part 2 test results should match expectation") {
            part2TestResult shouldBe testResults.part2
        }
        println("✅ Part 2 Test Input: $part2TestResult")
        "Part 2: ${part2(input)}".println()
    }
}

data class ExpectedTestInputResults<Output>(
        val part1: Output,
        val part2: Output,
)

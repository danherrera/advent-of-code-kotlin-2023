import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day7Test {

    @Test
    fun `test hand strengths with wildcards`() {
        mapOf(
            "AAAAA" to 7,
            "AAAAJ" to 7,
            "AAAA4" to 6,
            "AAAJ4" to 6,
            "AAJJ4" to 6,
            "AAA44" to 5,
            "AAJ44" to 5,
            "AAA43" to 4,
            "AAK44" to 3,
            "AQK44" to 2,
            "AKT8J" to 2,
            "AKT84" to 1,
        ).forEach { (hand, strength) ->
            withClue("$hand should have strength $strength") {
                handStrengthWithWildcards(hand) shouldBe strength
            }
        }
    }

    @Test
    fun `test ordering near identical four of a kind with wildcard`() {
        val challenge = Day7()
        val input = """
            44443 8
            444J3 9
        """.trimIndent().split("\n")
        val result = challenge.part2(input)
        result shouldBe (1 * 9 + 2 * 8)
    }

    @Test
    fun `test ordering near identical four of a kind with wildcard different order`() {
        val challenge = Day7()
        val input = """
            23333 8
            323J3 9
        """.trimIndent().split("\n")
        val result = challenge.part2(input)
        result shouldBe (1 * 9 + 2 * 8)
    }

    @Test
    fun `test ordering near identical three of a kind with wildcard`() {
        val challenge = Day7()
        val input = """
            54443 8
            544J3 9
        """.trimIndent().split("\n")
        val result = challenge.part2(input)
        result shouldBe (1 * 9 + 2 * 8)
    }
}

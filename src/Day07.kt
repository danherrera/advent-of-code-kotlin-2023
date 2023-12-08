fun main() = Day7().run()

class Day7 : Challenge<Long>(7) {
    override val testResults = ExpectedTestInputResults<Long>(
        part1 = 6440L,
        part2 = 5905L,
    )

    override fun part1(input: List<String>): Long {
        return input.map(Play::from)
            .sortedWith { a, b ->
                val aStrength = handStrength(a.cards)
                val bStrength = handStrength(b.cards)
                when {
                    aStrength > bStrength -> 1
                    aStrength < bStrength -> -1
                    else -> {
                        for ((i, aChar) in a.cards.withIndex()) {
                            val aCardStrength = cardStrengths.indexOf(aChar)
                            val bCardStrength = cardStrengths.indexOf(b.cards[i])
                            return@sortedWith when {
                                aCardStrength > bCardStrength -> 1
                                aCardStrength < bCardStrength -> -1
                                else -> continue
                            }
                        }
                        return@sortedWith 0
                    }
                }
            }
            .mapIndexed { index, play ->
                (play.bid * (index + 1)).toLong()
            }
            .sum()
    }

    override fun part2(input: List<String>): Long {
        return input.map(Play::from)
            .sortedWith { a, b ->
                val aStrength = handStrengthWithWildcards(a.cards)
                val bStrength = handStrengthWithWildcards(b.cards)
                when {
                    aStrength > bStrength -> 1
                    aStrength < bStrength -> -1
                    else -> {
                        val sortedA = a.cards.toCharArray()
                            .sortedBy {
                                cardStrengthsWithWildCards.size - cardStrengthsWithWildCards.indexOf(it)
                            }
                            .toString()
                        val sortedB = b.cards.toCharArray()
                            .sortedBy {
                                cardStrengthsWithWildCards.size - cardStrengthsWithWildCards.indexOf(it)
                            }
                            .toString()
                        for ((i, aChar) in sortedA.withIndex()) {
                            val aCardStrength = cardStrengthsWithWildCards.indexOf(aChar)
                            val bCardStrength = cardStrengthsWithWildCards.indexOf(sortedB[i])
                            return@sortedWith when {
                                aCardStrength > bCardStrength -> 1
                                aCardStrength < bCardStrength -> -1
                                else -> continue
                            }
                        }
                        return@sortedWith 0
                    }
                }
            }
            .println()
            .mapIndexed { index, play ->
                (play.bid * (index + 1)).toLong()
            }
            .sum()
    }

}

val cardStrengths = ('2'..'9').toList() + listOf('T', 'J', 'Q', 'K', 'A')
val cardStrengthsWithWildCards = listOf('J') + ('2'..'9').toList() + listOf('T', 'Q', 'K', 'A')

fun handStrength(hand: String): Int {
    val charGroups = hand.groupBy { it }.values
    return when {
        // five of a kind
        hand.toSet().size == 1 -> 7
        // four of a kind
        charGroups.maxOf { it.size } == 4 -> 6
        // full house
        charGroups.sortedBy { it.size }.run { first().size == 2 && last().size == 3 } -> 5
        // three of a kind
        charGroups.maxOf { it.size } == 3 -> 4
        // two pair
        charGroups.sortedBy { it.size }.drop(1).run { first().size == 2 && last().size == 2 } -> 3
        // one pair
        charGroups.any { it.size == 2 } -> 2
        // high card
        else -> 1
    }
}

fun handStrengthWithWildcards(hand: String): Int {
    val groups = hand.groupBy { it }
    val wildCards = groups['J'].orEmpty()
    if (wildCards.isEmpty()) return handStrength(hand)
    val charGroups =
        groups.values.dropWhile { chars -> chars.all { it == 'J' } }.sortedBy { it.size }
    return when {
        // five of a kind
        charGroups.isEmpty() || charGroups.last().size + wildCards.size == 5 -> 7
        // four of a kind
        charGroups.last().size + wildCards.size == 4 -> 6
        // full house
        charGroups.run { wildCards.size >= 3 || size == 2 || (size == 3 && first().first() == 'J') } -> 5
        // three of a kind
        charGroups.run { last().size >= 2 || wildCards.size >= 2 } -> 4
        // two pair
        charGroups.run { last().size >= 2 } -> 3
        // one pair
        else -> 2
    }
}

data class Play(val cards: String, val bid: Int) {
    companion object {
        fun from(play: String): Play = Play(
            play.substringBefore(" "),
            play.substringAfter(" ").toInt()
        )
    }
}

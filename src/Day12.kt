import kotlin.math.pow

fun main() = Day12().run()
class Day12 : Challenge<Long>(12) {
    override val testResults = ExpectedTestInputResults<Long>(
            21L,
            525152L,
    )

    private val numberOfKnownCharacters = 2 // . or #

    private fun numberOfKnownCharacterVariations(length: Int): Int {
        return numberOfKnownCharacters.toDouble().pow(length.toDouble()).toInt()
    }

    private fun variationForVariationIndex(variationIndex: Int, length: Int): String {
        return Integer.toBinaryString(variationIndex)
                .replace('0', '.')
                .replace('1', '#')
                .padStart(length, '.')
    }

    private fun knownCharacterVariations(length: Int): List<String> {
        return (0 until numberOfKnownCharacterVariations(length))
                .map { n ->
                    variationForVariationIndex(n, length)
                }
    }

    private fun String.replaceUnknowns(variation: String): String {
        return variation.fold(this) { acc, c ->
            acc.replaceFirst('?', c)
        }
    }

    private fun String.structure(): List<Int> {
        return split("\\.+".toRegex()).map { it.length }.filter { it > 0 }
    }

    override fun part1(input: List<String>): Long {
        return input.map { line ->
            line.split(" ")
                    .run { first() to last().split(",").map { it.toInt() } }
        }.sumOf { (format, structure) ->
            val numberOfUnknowns = format.count { it == '?' }
            val variations = knownCharacterVariations(numberOfUnknowns)
            var structureMatches = 0L
            for (variation in variations) {
                val variationStructure = format.replaceUnknowns(variation).structure()
                if (variationStructure == structure) {
                    structureMatches++
                }
            }
            structureMatches
        }
    }

    private fun String.formatUnfolded(): String {
        return List(5) { this }.joinToString("?")
    }

    private fun String.structureUnfolded(): String {
        return List(5) { this }.joinToString(",")
    }

    override fun part2(input: List<String>): Long {
        return input.map { line ->
            line.split(" ").run {
                first().formatUnfolded() to last().structureUnfolded().split(",").map { it.toInt() }
            }
        }.flatMap { (format, structure) ->
            val numberOfUnknowns = format.count { it == '?' }
//            val variations = knownCharacterVariations(numberOfUnknowns)
//            variations.map { variation ->
//                structure to format.replaceUnknowns(variation).structure()
//            }
            (0 until numberOfUnknowns).map {
                structure to format.replaceUnknowns(variationForVariationIndex(it, numberOfUnknowns)).structure()
            }
        }.fold(0L) { acc, (expectedStructure, structureForVariation) ->
            if (expectedStructure == structureForVariation) acc + 1
            else acc
        }
    }

}
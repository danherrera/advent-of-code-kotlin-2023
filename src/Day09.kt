fun main() = day(9) {
    part(1) { input: List<String> ->
        expect(sample = 114)
        input
                .map { line ->
                    line.split(" ").map { it.toInt() }
                }
                .map { numbers ->
                    val layers = mutableListOf(numbers)
                    while (layers.last().any { it != 0 }) {
                        layers.add(layers.last().differences())
                    }
                    layers
                }.sumOf { layers ->
                    layers.sumOf { it.last() }
                }
    }

    part(2) { input: List<String> ->
        expect(sample = 2)
        input
                .map { line ->
                    line.split(" ").map { it.toInt() }
                }
                .map { numbers ->
                    val layers = mutableListOf(numbers)
                    while (layers.last().any { it != 0 }) {
                        layers.add(layers.last().differences())
                    }
                    layers
                }.sumOf { layers ->
                    val mutableLayers = layers.map { it.toMutableList() }
                    var previousValue = 0
                    var layerIndex = layers.lastIndex - 1
                    while (layerIndex >= 0) {
                        previousValue = mutableLayers[layerIndex].first() - previousValue
                        mutableLayers[layerIndex].add(0, previousValue)
                        layerIndex--
                    }
                    previousValue
                }
    }
}


private fun List<Int>.differences(): List<Int> {
    return windowed(2).fold(emptyList<Int>()) { acc, pair ->
        acc + pair.run { last() - first() }
    }
}
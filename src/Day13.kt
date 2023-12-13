fun main() = day(13) {
    part(1) { input: List<String> ->
        expect(sample = 405)
        input.fold(mutableListOf(mutableListOf<String>())) { acc, s ->
            if (s.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(s)
            }
            acc
        }.map { Group.from(it) }.sumOf { group ->
            group.beforeReflection(group.rowHashes)?.let { it * 100 }
                    ?: group.beforeReflection(group.columnHashes)!!
        }
    }
}

data class Group(
        val rows: Int,
        val columns: Int,
        val data: List<String>,
) {
    companion object {
        fun from(groupInput: List<String>): Group {
            return Group(
                    rows = groupInput.size,
                    columns = groupInput.first().length,
                    data = groupInput,
            )
        }
    }

    val rowHashes: List<Int> = data.map { it.hashCode() }
    val columnHashes: List<Int> = (0 until columns)
            .map { columnIndex ->
                data.map { row -> row[columnIndex] }
                        .joinToString("")
                        .hashCode()
            }

    fun beforeReflection(hashes: List<Int>): Int? {
        mirrowLocationCheck@ for (i in hashes.indices) {
            val left = hashes.subList(0, i).reversed()
            val right = hashes.subList(i, hashes.size)
            if (left.isEmpty() || right.isEmpty()) continue
            for ((hashIndex, leftHash) in left.withIndex()) {
                if (hashIndex == right.lastIndex && leftHash == right.last()) return left.size
                if (leftHash != right[hashIndex]) continue@mirrowLocationCheck
            }
            return left.size
        }
        return null
    }
}
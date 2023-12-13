import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val data = mutableListOf<List<String>>()
        val pattern = mutableListOf<String>()
        input.forEach {
            if (it.isBlank()) {
                data.add(pattern.toList())
                pattern.clear()
            } else {
                pattern.add(it)
            }
        }
        if (pattern.isNotEmpty()) data.add(pattern.toList())


        val result = data.map { pattern ->
            val horizontal = pattern.map { it.findMirror(Direction.HORIZONTAL) }
            val horizontalMirror =
                horizontal.flatten().toSet().find { mirrorToLookAt -> horizontal.all { it.contains(mirrorToLookAt) } }

            val traversedData = pattern.first().indices.map { i ->
                pattern.map { it[i] }.joinToString("")
            }

            val vertical = traversedData.map { it.findMirror(Direction.VERTICAL) }
            val verticalMirror =
                vertical.flatten().toSet().find { mirrorToLookAt -> vertical.all { it.contains(mirrorToLookAt) } }


            (horizontalMirror?.second ?: 0) + (100 * (verticalMirror?.second ?: 0))
        }

        return result.sum()
    }

    fun part2(input: List<String>): Int {
        val data = mutableListOf<List<String>>()
        val pattern = mutableListOf<String>()
        input.forEach {
            if (it.isBlank()) {
                data.add(pattern.toList())
                pattern.clear()
            } else {
                pattern.add(it)
            }
        }
        if (pattern.isNotEmpty()) data.add(pattern.toList())


        val result = data.map { pattern ->
            val horizontal = pattern.map { it.findMirror(Direction.HORIZONTAL) }

            val counts = horizontal.flatten().groupingBy { it.second }.eachCount()

            val possibleCounts  = counts.filterValues { it == pattern.size -1 }
            val horizontalValue = possibleCounts.keys.firstOrNull() ?: 0

            val traversedData = pattern.first().indices.map { i ->
                pattern.map { it[i] }.joinToString("")
            }

            val vertical = traversedData.map { it.findMirror(Direction.VERTICAL) }

            val verticalCounts = vertical.flatten().groupingBy { it.second }.eachCount()

            val verticalPossibleCounts = verticalCounts.filterValues { it == traversedData.size -1}
            val verticalValue = verticalPossibleCounts.keys.firstOrNull() ?: 0

            horizontalValue + (100 * (verticalValue))
        }

        return result.sum()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
//    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}


fun String.findDifferences() {
    var results = mutableListOf<Int>()
    for (i in 1 until this.length) {
        val (left, right) = this.withIndex().partition { it.index < i }.let {
            Pair(it.first.map { it.value }, it
                .second.map { it.value })
        }
        val length = min(left.size, right.size)
        val leftSub = left.subList(left.size - length, left.size)
        val rightSub = right.subList(0, length)

        val differences = leftSub.zip(rightSub.reversed()).count { (a, b) -> a != b }
        if (differences == 1) {
            println("DIFF = 1 here")
        }
    }
}

fun String.findMirror(direction: Direction): List<Pair<Direction, Int>> {
    var results = mutableListOf<Pair<Direction, Int>>()
    for (i in 1 until this.length) {
        val (left, right) = this.withIndex().partition { it.index < i }.let {
            Pair(it.first.map { it.value }, it
                .second.map { it.value })
        }
        val length = min(left.size, right.size)
        val leftSub = left.subList(left.size - length, left.size)
        val rightSub = right.subList(0, length)
        if (leftSub == rightSub.reversed()) {
            results.add(Pair(direction, i))
        }
    }
    return results
}


enum class Direction {
    HORIZONTAL,
    VERTICAL
}
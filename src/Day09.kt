import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val numbers = line.split(" ").map { it.toLong() }
            val last = sequence(numbers, 0).last()
            last
        }
    }


    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val numbers = line.split(" ").map { it.toLong() }
            val last = sequence(numbers, 0).last()
            last
        }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun sequence(input: List<Long>, step: Int = 0): List<Long> {
    var nextSequence = input.windowed(2, 1).map { (a, b) -> b - a }
    if (nextSequence.isEmpty()) {
        nextSequence = listOf(0)
    }
    return if (nextSequence.all { it == 0L }) {
        runCatching {
            input + input.last().let { it + nextSequence.last() }
        }.onFailure {
            println(nextSequence)
        }.getOrThrow()
    } else {
        val newSequenceFromLower = sequence(nextSequence, step.inc())
        input + input.last().let { it + newSequenceFromLower.last() }
    }
}
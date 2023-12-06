import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val times = input[0].split("\\s+".toRegex()).drop(1).map { it.toLong() }
        val distances = input[1].split("\\s+".toRegex()).drop(1).map { it.toLong() }

        val result = times.zip(distances).map { (time, distance) ->
            val fromLower = (0 until time).asSequence().associateWith { holdTime ->
                (time - holdTime) * holdTime
            }.entries.takeWhile { it.value <= distance }.last().key + 1
            val fromUpper = (time downTo 0).asSequence().associateWith { holdTime ->
                (time - holdTime) * holdTime
            }.entries.takeWhile { it.value <= distance }.last().key - 1
            abs(fromLower - fromUpper) + 1L
        }
        return result.fold(1L) { acc, l -> acc * l }
    }

    fun part2(input: List<String>): Long {
        val time = input[0].split("\\s+".toRegex()).drop(1).joinToString("").toLong()
        val distance = input[1].split("\\s+".toRegex()).drop(1).joinToString("").toLong()
        return part1(listOf("Time: $time", "Distance: $distance"))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

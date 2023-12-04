import java.lang.Integer.min
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {

        val result = input
            .asSequence()
            .map { it.split(": ").component2().replace("  ", " ") }
            .map { it.split(" | ").let { Pair(it.component1().split(" "), it.component2().split(" ")) } }
            .map { it.first intersect it.second }
            .filter { it.isNotEmpty() }
            .map { Math.pow(2.0, it.size - 1.0) }
            .toList()

        return result.sum().toInt()
    }

    fun part2(input: List<String>): Int {

        val sc = Array(input.size) { 1 } // start with 1 of each
        input
            .asSequence()
            .map { it.split(": ").component2().replace("  ", " ") }
            .map { it.split(" | ").let { Pair(it.component1().split(" "), it.component2().split(" ")) } }
            .map { Pair(it.first.filter { it.isNotEmpty() }, it.second.filter { it.isNotEmpty() }) }
            .map { it.first intersect it.second }
            .map { it.count() }
            .mapIndexed { index, winCount ->
                val amount = sc[index]
                IntRange(index + 1, min(index + winCount, input.size)).forEach {
                    sc[it] = sc[it] + amount
                }
            }
            .toList()
        return sc.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    measureTimeMillis {
        part1(input).println()
    }.let { println("Took ${it} ms") }

    measureTimeMillis {
        part2(input).println()
    }.let { println("Took ${it} ms") }

}

import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val (line, numbers) = it.split(" ").let { Pair(it.first(), it[1].split(",").map { it.toInt() }) }
            val count = line.count { it == '?' }
            val combinations = (0 until 2.power(count)).map { i ->
                i.toString(2).padStart(count, '0').replace("0", "#").replace("1", ".")
            }.map {
                it.fold(line) { acc, replacement -> acc.replaceFirst('?', replacement) }
            }.toSet().filter {
                val groups = Regex("(#+)").findAll(it).map { it.range.let { it.last + 1 - it.first } }.toList()
                numbers == groups
            }
            combinations.size
        }.sum()
    }

    fun part2(input: List<String>): Int {

        val data = input.asSequence().map {
            val (line, numbers) = it.split(" ").let { Pair(it.first().repeatWithSep(5,"?"), it[1].split(",").repeat(5)
                .map { it.toInt()
            }) }
            val calculation = line.calculate(numbers)
            cache.clear()
            calculation
        }.sum()

        return data
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part2(testInput) == 525152)

    val input = readInput("Day12")
    measureTimeMillis { part1(input).println() }.let { println("took $it ms") }

    measureTimeMillis { part2(input).println() }.let { println("took $it ms") }
}


val cache = mutableMapOf<String, Int>()
fun String.calculate(numbers: List<Int>): Int {
    return cache.getOrPut(this + numbers.joinToString(",")) {
        if (this.contains("?")) {
            this.replaceFirst('?', '#').calculate(numbers) + this.replaceFirst('?', '.').calculate(numbers)
        } else {
            val groups = Regex("(#+)").findAll(this).map { it.range.let { it.last + 1 - it.first } }.toList()
            return if (numbers == groups) {
                1
            } else {
                0
            }
        }
    }
}

infix fun Int.power(p: Int) = (0 until p).fold(this) { acc, _ -> acc * this }

fun <T> List<T>.repeat(n: Int): List<T> = (0 until n).flatMap { this }
fun String.repeatWithSep(n: Int, separator: String): String = (0 until n).map { this }.joinToString(separator)
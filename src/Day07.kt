import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Long {
        val result = input.map { it.split(" ").let { Pair(it[0], it[1].toInt()) } }.sortedWith(::sort).toList()
        return result.mapIndexed { index, (_, bid) ->
            index.inc() * bid.toLong()
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val result = input.map { it.split(" ").let { Pair(it[0], it[1].toInt()) } }.sortedWith(::sort2).toList()
        return result.mapIndexed { index, (_, bid) ->
            index.inc() * bid.toLong()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    check(part1(input) == 241344943L)

    part1(input).println()
    measureTimeMillis {
        part2(input).println()
    }.let { println("${it} ms") }
}

fun sort(pair: Pair<String, Int>?, pair1: Pair<String, Int>?): Int {
    val hand1 = pair!!.first
    val handType = hand1.getHandStrength()
    val hand2 = pair1!!.first
    val handType2 = hand2.getHandStrength()
    return if (handType == handType2) {
        val values1 = hand1.map { it.toValue() }
        val values2 = hand2.map { it.toValue() }
        values1.zip(values2).dropWhile { (l, r) -> l == r }.firstOrNull()?.let { (l, r) -> l.compareTo(r) } ?: 0
    } else {
        handType.compareTo(handType2)
    }
}

fun sort2(pair: Pair<String, Int>?, pair1: Pair<String, Int>?): Int {
    val hand1 = pair!!.first
    val handType = hand1.getHandStrength(true)
    val hand2 = pair1!!.first
    val handType2 = hand2.getHandStrength(true)
    return if (handType == handType2) {
        val values1 = hand1.map { it.toValue(true) }
        val values2 = hand2.map { it.toValue(true) }
        values1.zip(values2).dropWhile { (l, r) -> l == r }.firstOrNull()?.let { (l, r) -> l.compareTo(r) } ?: 0
    } else {
        handType.compareTo(handType2)
    }
}


/**
 * Assign each type of hand to a numeric value
 * 6 -> Five of a kind
...
 * 0 -> high card
 */
fun String.getHandStrength(part2: Boolean = false): Int {
    var occurences = this.groupingBy { it }.eachCount()
    val jOffset = if (part2) occurences.getOrDefault('J', 0) else 0

    if (jOffset == 5) {
        return 6
    }

    if (part2) occurences = occurences.filterKeys { it != 'J' }


    return when {
        occurences.any { it.value + jOffset == 5 } -> 6
        occurences.any { it.value + jOffset == 4 } -> 5
        occurences.filter { it.value == 2 }.count() == 2 && jOffset == 1 -> 4
        occurences.any { it.value == 3 } && occurences.any { it.value == 2 } -> 4
        occurences.any { it.value + jOffset == 3 } -> 3
        occurences.filter { it.value == 2 }.count() == 2 -> 2
        occurences.any { it.value + jOffset == 2 } -> 1
        else -> 0
    }
}

fun Char.toValue(part2: Boolean = false): Int = when {
    this.isDigit() -> this.digitToInt()
    this == 'T' -> 10
    this == 'J' -> if (part2) 0 else 11
    this == 'Q' -> 12
    this == 'K' -> 13
    this == 'A' -> 14
    else -> 0
}
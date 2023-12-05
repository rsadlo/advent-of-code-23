import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val seeds = input.seeds()

        val lambdas = listOf(
            "seed-to-soil",
            "soil-to-fertilizer",
            "fertilizer-to-water",
            "water-to-light",
            "light-to-temperature",
            "temperature-to-humidity",
            "humidity-to-location"
        ).map { input.findMap(it) }

        val result = seeds.map {
            lambdas.fold(it) { acc, l -> l.invoke(acc) }

        }

        return result.min().toInt()
    }

    fun part2(input: List<String>): Int {
        //This takes over 5 mins to execute. There must be a better way of implementing this...
        // but as always there is real work to be done instead of aoc...
        val seeds = input.seedRanges()

        val lambdas = listOf(
            "seed-to-soil",
            "soil-to-fertilizer",
            "fertilizer-to-water",
            "water-to-light",
            "light-to-temperature",
            "temperature-to-humidity",
            "humidity-to-location"
        ).map { input.findMap(it) }


        var final = Long.MAX_VALUE

        seeds.forEach {
            //Moved from map operations to for each loop, since the other produced memory issues
            //It would probably work with sequence and stateless operations only: TODO: Rewrite with sequence and
            // without min()
            for (i in it) {
                val result = lambdas.fold(i) { acc, l -> l.invoke(acc) }
                final = min(final, result)
            }
        }
        return final.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}


fun List<String>.seeds() = this[0].split(": ")[1].split(" ").map { it.toLong() }


fun List<String>.seedRanges(): List<LongRange> {
    val seedValues = this[0].split(": ")[1].split(" ")
    return seedValues.windowed(2, 2).map {
        LongRange(it[0].toLong(), it[0].toLong() + it[1].toLong() - 1)
    }
}


/**
 * Create a lambda, which applies the transform to the given input
 */
fun List<String>.findMap(name: String): (Long) -> Long {
    val index = this.indexOfFirst { it.startsWith(name) && it.endsWith("map:") }

    val ranges = this.subList(index + 1, this.size).takeWhile { it.isNotEmpty() }.map {
        val (outStart, inStart, length) = it.split(" ").map { it.toLong() }
        Pair(LongRange(inStart, inStart + length - 1), LongRange(outStart, outStart + length - 1))
    }
    return { i: Long ->
        val range = ranges.firstOrNull { it.first.contains(i) }
        if (range == null) {
            i
        } else {
            val rangeOffset = i - range.first.first
            range.second.first + rangeOffset
        }
    }
}
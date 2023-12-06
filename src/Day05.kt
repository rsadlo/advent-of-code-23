import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

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


        val result = runBlocking {
            withContext(Executors.newFixedThreadPool(64).asCoroutineDispatcher()) {
                seeds.flatMap { seeds ->
                    seeds.asSequence().chunked(100000).map { seed ->
                            async {
                                seed.map { lambdas.fold(it) { acc, l -> l.invoke(acc) } }.min()
                            }
                        }
                }
            }.awaitAll().min()
        }
        return result.toInt()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46)

    val input = readInput("Day05")

    measureNanoTime { part1(input).println() }.let { println("Took $it ns") }
    println("---")
    measureTimeMillis { part2(input).println() }.let { println("Took $it ms") }
}


fun List<String>.seeds() = this[0].removePrefix("seeds: ").split(" ").map { it.toLong() }


fun List<String>.seedRanges(): List<LongRange> {
    val seedValues = this[0].removePrefix("seeds: ").split(" ")
    return seedValues.windowed(2, 2).map {
        it[0].toLong()..<it[0].toLong() + it[1].toLong()
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

    val map = ranges.associate { range -> Pair(range.first, (range.second.first - range.first.first)) }

    return { i: Long ->
        map.entries.firstOrNull { it.key.contains(i) }?.let { i + it.value } ?: i
    }
}
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val fieldValues = Array(input.size) {row ->
            Array(input[row].length) {col ->
                1
            }
        }

        input.forEachIndexed { row, line ->
            if(line.none { it == '#' }) {
                for(col in line.indices){
                    fieldValues[row][col] = 2
                }
            }
        }

        input.first().forEachIndexed { col, c ->
            if( input.map { it[col] }.none { it == '#' } ) {
                for(row in input.indices) {
                    fieldValues[row][col] = 2
                }
            }
        }

        fieldValues.forEach { println(it.joinToString ("\t") ) }

        val galaxies = input.flatMapIndexed { idx,it -> "\\#".toRegex().findAll(it).map { Point(idx, it.range.first) } }


        println(galaxies)

        val distnaces = galaxies.mapIndexed { index, galaxy1 ->
            val distances = galaxies.subList(index +1,galaxies.size).map {galaxy2 ->
                val row = galaxy1.first
                val col = galaxy2.second
                val yDistance = (min(galaxy1.first,galaxy2.first) until max(galaxy1.first,galaxy2.first)).map { r ->
                    fieldValues[r][col]
                }.sum()

                val xDistance = (min(galaxy1.second,galaxy2.second) until max(galaxy1.second,galaxy2.second)).map { c ->
                    fieldValues[row][c]
                }.sum()

                xDistance + yDistance
            }
            distances.sum()
        }


        return distnaces.sum()
    }

    fun part2(input: List<String>): Long {
        val fieldValues = Array(input.size) {row ->
            Array(input[row].length) {col ->
                1L
            }
        }

        input.forEachIndexed { row, line ->
            if(line.none { it == '#' }) {
                for(col in line.indices){
                    fieldValues[row][col] = 1000000L
                }
            }
        }

        input.first().forEachIndexed { col, c ->
            if( input.map { it[col] }.none { it == '#' } ) {
                for(row in input.indices) {
                    fieldValues[row][col] = 1000000L
                }
            }
        }

        fieldValues.forEach { println(it.joinToString ("\t") ) }

        val galaxies = input.flatMapIndexed { idx,it -> "\\#".toRegex().findAll(it).map { Point(idx, it.range.first) } }


        println(galaxies)

        val distnaces = galaxies.mapIndexed { index, galaxy1 ->
            val distances = galaxies.subList(index +1,galaxies.size).map {galaxy2 ->
                val row = galaxy1.first
                val col = galaxy2.second
                val yDistance = (min(galaxy1.first,galaxy2.first) until max(galaxy1.first,galaxy2.first)).map { r ->
                    fieldValues[r][col]
                }.sum()

                val xDistance = (min(galaxy1.second,galaxy2.second) until max(galaxy1.second,galaxy2.second)).map { c ->
                    fieldValues[row][c]
                }.sum()

                xDistance + yDistance
            }
            distances.sum()
        }


        return distnaces.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)

    val input = readInput("Day11")
    part1(input).println()
    measureTimeMillis { part2(input).println() }.let { println("took $it ms") }

}

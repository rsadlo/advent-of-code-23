typealias Point = Pair<Int, Int>

enum class DIRECTIONS(val offset: Point) {
    N(Point(-1, 0)), E(Point(0, 1)), S(Point(1, 0)), W(Point(0, -1))
}

val mapping = mapOf(
    '|' to Pair(DIRECTIONS.N, DIRECTIONS.S),
    '-' to Pair(DIRECTIONS.E, DIRECTIONS.W),
    'L' to Pair(DIRECTIONS.N, DIRECTIONS.E),
    'J' to Pair(DIRECTIONS.N, DIRECTIONS.W),
    '7' to Pair(DIRECTIONS.W, DIRECTIONS.S),
    'F' to Pair(DIRECTIONS.E, DIRECTIONS.S),
)

fun Point.adjacent(rowLim: Int? = null, colLim: Int? = null): List<Point> {
    return listOf(Point(0, 1), Point(1, 0), Point(-1, 0), Point(0, -1)).map {
        Point(
            this.first + it.first,
            this.second + it.second
        )
    }.filter { it.first >= 0 && it.second >= 0 }.filter { rowLim?.let { this.first < it } ?: true }
        .filter { colLim?.let { this.second < colLim } ?: true }
}

fun main() {

    val path = mutableSetOf<Point>()

    fun part1(input: List<String>): Int {

        val distances: Array<Array<Int>> = Array(input.size) { col ->
            Array(input.first().length) { row ->
                -1
            }
        }

        val start = input.indexOfFirst { it.contains("S") }.let { Pair(it, input[it].indexOfFirst { it == 'S' }) }
        distances[start.first][start.second] = 0
        path.add(start)

        val toLookAt = start.adjacent(input.size, input.first().length).map {
            Triple(it, start, 0)
        }.filter { (position, start, _) ->
            val pipe = input[position.first][position.second]
            if (pipe == '.') false
            else {
                mapping.getValue(pipe).toList().map {
                    Point(
                        position.first + it.offset.first, position.second + it.offset.second
                    )
                }.filter { it == start }.isNotEmpty()
            }
        }.toMutableList()

        while (toLookAt.isNotEmpty()) {
            val next = toLookAt.removeFirst()
            path.add(next.first) // Add the Point to the path array, since we a
            distances.path(next.first, next.second, input, next.third)?.let {
                toLookAt.add(it)
            }
        }


        distances.forEach { it.joinToString("\t").println() }
        return -1 * distances.minOf { distances.minOf { it.min() } }
    }

    fun part2(input: List<String>): Int {

        var count = 0

        val filteredPath = path.filter { input[it.first][it.second].let { !listOf('L','J','-').contains(it) } }

        input.forEachIndexed { row, rowContent ->
            rowContent.forEachIndexed { col, field ->
                val position = Point(row, col)
                if (!path.contains(position)) {
                    val hits = filteredPath.count { it.first == position.first && it.second > position.second }
                    if (hits % 2 == 1) {
                        count = count.inc()
                        print('x')
                    }else {
                        print('.')
                    }
                }else {
                    print(input[position.first][position.second].pretty())
                }
            }
            println()
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    part1(testInput) // required for path, lol
    check(part2(testInput) == 10)

    path.clear()
    val input = readInput("Day10")
    part1(input).println()
    println(path.size / 2)
    part2(input).println()
}


fun Array<Array<Int>>.path(
    position: Pair<Int, Int>, previous: Pair<Int, Int>, input: List<String>, distance: Int = 0
): Triple<Point, Point, Int>? {
    val newDistance = distance.inc()
    val pipe = input[position.first][position.second]

    if (pipe == '.') {
        return null
    }
    if (pipe == 'S') {
        return null
    }

    this[position.first][position.second].let {
        if (it != -1 && it < newDistance) {
            if (it == newDistance - 2) {
                this[previous.first][previous.second] = -newDistance.dec()
            }
            return null
        } else {
            this[position.first][position.second] = newDistance
        }
    }

    if (!mapping.containsKey(pipe)) {
        return null
    }

    val next = mapping.getValue(pipe).toList().map {
        Point(
            position.first + it.offset.first, position.second + it.offset.second
        )
    }.filter { it != previous }
    return next.firstOrNull()?.let {
        Triple(it, position, newDistance)
    }
}


fun Char.pretty() :Char= when(this) {
    'F' -> '╔'
    'J' -> '╝'
    'L' -> '╚'
    '7' -> '╗'
    '-' -> '═'
    '|' -> '║'
    else ->this
}
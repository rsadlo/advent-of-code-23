import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.first().map { instruction ->
            { input: Pair<String, String> ->
                when (instruction) {
                    'L' -> input.first
                    'R' -> input.second
                    else -> throw RuntimeException("unknown instruction")
                }
            }
        }

        val map = input.subList(2, input.size).map {
            it.split(" = ").let { (source, destinations) ->
                destinations.split(", ")
                    .map { it.replace("\\W".toRegex(), "") }
                    .let { d -> Pair(source, Pair(d[0], d[1])) }

            }
        }.toMap()


        var current = "AAA"
        var count = 0
        while (current != "ZZZ") {
            current = instructions[count % instructions.size].invoke(map.getValue(current))
            count = count.inc()
        }

        return count
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first().map { instruction ->
            { input: Pair<String, String> ->
                when (instruction) {
                    'L' -> input.first
                    'R' -> input.second
                    else -> throw RuntimeException("unknown instruction")
                }
            }
        }

        val map = input.subList(2, input.size).map {
            it.split(" = ").let { (source, destinations) ->
                destinations.split(", ")
                    .map { it.replace("\\W".toRegex(), "") }
                    .let { d -> Pair(source, Pair(d[0], d[1])) }

            }
        }.toMap()


        val STEP_SIZE = 1000000
        var current = map.keys.filter { it.endsWith("A") }.toMutableList()
        var count = 0L
        while (!current.all { it.endsWith("Z") }) {

            val zendings = current.mapIndexed { idx, start ->
                var next = start
                val zs = (0..<STEP_SIZE).map {
                    next = instructions[((count + it) % instructions.size.toLong()).toInt()].invoke(map.getValue(next))
                    Pair(count + it + 1, next.last())
                }.filter { it.second == 'Z' }
                current[idx] = next
                zs
            }
            val result = zendings.first().firstOrNull { idx -> zendings.all { it.contains(idx) } }
            if (count == 0.toLong()) {
                val divs = zendings.map { it.first().first }
                val max = divs.max()
                sequence {
                    var c = 0L
                    while(true){
                        c = c.inc()
                        yield(c)
                    }
                }.takeWhile {
                    val target = it * max
                    val hit = divs.all { target % it == 0L }
                    !hit
                }.last().let {
                    return (it+1) * max
                }
            }

            if (result == null) {
                count += STEP_SIZE
            } else {
                return result.first
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    measureTimeMillis { part2(input).println() }.let { println("took $it ms") }

}

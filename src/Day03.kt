fun main() {
    fun part1(input: List<String>): Int {
        val mask: Array<Array<Char>> = Array(input.size) { l ->
            Array(input.first().length) { c ->
                input[l][c].let {
                    if (input.adjacentIs(l, c) { it, _, _ -> it != '.' && !it.isDigit() }) {
                        '*'
                    } else {
                        '.'
                    }
                }
            }
        }

        val result = input.flatMapIndexed { line, s ->
            "(\\d+)".toRegex().findAll(s).map { result ->
                result.groups[0].let { group ->
                    if (mask[line].hasInRange(group!!.range) { it == '*' }) group.value
                    else null
                }
            }.filterNotNull()
        }.sumOf { it.toInt() }
        return result
    }

    /**
     * FML... TODO: Optimize this. At least its working
     */
    fun part2(input: List<String>): Int {
        val mask: Array<Array<Char>> = Array(input.size) { l ->
            Array(input.first().length) { c ->
                //In The Mask we now look for Gears and adjacent gears
                if (input.isGear(l, c)) {
                    'G'
                } else if (input.adjacentIs(l, c) { _, l, c -> input.isGear(l, c) }) {
                    'g'
                } else {
                    '.'
                }
            }
        }

        mask.print()

        // Find all numbers, that have a g (gear contact) on mask
        val gearNumbers = input.flatMapIndexed { line, s ->
            "(\\d+)".toRegex().findAll(s).map { result ->
                result.groups[0].let { group ->
                    if (mask[line].hasInRange(group!!.range) { it == 'g' }) {
                        (Triple(group.value.toInt(), line, group.range))
                    } else null
                }
            }.filterNotNull()
        }


        return gearNumbers.mapNotNull { (num, l, c) ->
            // Find the G (ear) location, which belongs to the number
            c.firstNotNullOfOrNull { mask.find(l, it) { it == 'G' } }
                ?.let { Pair(it, num) }
            //Group all the numbers, which have the same G(ear) and multiply
        }.groupBy { it.first }
            .mapValues { it.value.map { it.second }.fold(1) { acc, i -> acc * i } }
            .values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun Array<Array<Char>>.print() = this.forEach { println(it.joinToString("")) }

fun Array<Char>.hasInRange(range: IntRange, check: (Char) -> Boolean): Boolean =
    range.map { this[it] }.any { check(it) }

fun List<String>.adjacentIs(l: Int, c: Int, check: (Char, Int, Int) -> Boolean): Boolean {
    val input = listOf(-1, 0, 1)
    return input.flatMap { lO -> input.map { cO -> Pair(l + lO, c + cO) } }
        .filter { (l, c) -> 0 < l && l < this.size && 0 < c && c < this.first().length }
        .any { (l, c) -> check(this[l][c], l, c) }
}

fun List<String>.isGear(l: Int, c: Int): Boolean {
    if (this[l][c] != '*') return false
    val input = listOf(-1, 0, 1).map { l + it }.filter { 0 <= it && it < this.size }
    val numbersInLine = input.flatMap {
        val focus = this[it].substring(maxOf(0, c - 1), minOf(this[0].length, c + 2))
        focus.split(".", "*")
    }.filter { it.isNotEmpty() }
    return numbersInLine.count() == 2
}

fun Array<Array<Char>>.find(l: Int, c: Int, check: (Char) -> Boolean): Pair<Int, Int>? {
    val input = listOf(-1, 0, 1)
    return input.flatMap { lO -> input.map { cO -> Pair(l + lO, c + cO) } }
        .filter { (l, c) -> 0 < l && l < this.size && 0 < c && c < this.first().size }
        .firstOrNull { (l, c) -> check(this[l][c]) }

}
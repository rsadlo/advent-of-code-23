fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
                "${it.first(Char::isDigit)}${it.last(Char::isDigit)}".toInt()
            }
    }

    fun part2(input: List<String>): Int {
        return part1(input.map { it.replace(MAPPING) })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}



/**
 * With combinations like eightwothree we can not simply replace, since
 * eighTwo contains 2 digits, whereas the 2 would be replaced first, killing the 8.
 * So we use this workaround to replace, but keep start and end letters for other digits
 */
val MAPPING = mapOf(
    "one" to "on1e",
    "two" to "t2wo",
    "three" to "th3ree",
    "four" to "fo4ur",
    "five" to "fi5ve",
    "six" to "s6ix",
    "seven" to "se7ven",
    "eight" to "ei8ght",
    "nine" to "ni9ne",
)


/**
 * calls replace for every entry in the map
 */
fun String.replace(mapping: Map<String, String>): String = mapping.entries.fold(this) { acc, replacement ->
    acc.replace(replacement.key, replacement.value)
}


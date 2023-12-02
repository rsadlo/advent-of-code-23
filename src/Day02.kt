fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val (game, roundsInput) = it.split(": ")
            val gameNumber = game.substring(5).toInt()
            val rounds = roundsInput.split("; ").map { roundString ->
                roundString.split(",")
                    .map { it.trim() }
                    .map { Dice.fromString(it) }
            }
            val anyInvalid = rounds.any { !it.all { it.validate() } }
            if (anyInvalid) 0 else gameNumber
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (_, roundsInput) = it.split(": ")
            val rounds = roundsInput.split("; ").map { roundString ->
                roundString.split(",")
                    .map { it.trim() }
                    .map { Dice.fromString(it) }
            }
            // Performance is bad, probably groupBy color and then mapValues would work better
            val maxPerColor = Color.entries.map { color ->
                rounds.flatten()
                    .filter { it.color == color }
                    .maxBy { it.num }
            }

            val totalMinMultiplied = maxPerColor
                .map { it.num }
                .multiply()
            totalMinMultiplied
        }
    }

    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

enum class Color(val max: Int) {
    RED(12),
    GREEN(13),
    BLUE(14);
}

data class Dice(
    val color: Color,
    val num: Int
) {
    fun validate(): Boolean = num <= color.max

    companion object {
        fun fromString(input: String): Dice {
            val (num, color) = input.trim().split(" ")
            return Dice(Color.valueOf(color.uppercase()), num.toInt())
        }
    }
}


fun Collection<Int>.multiply() = this.fold(1) { acc, i -> acc * i }
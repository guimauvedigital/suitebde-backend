package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class AddDurationUseCase : IAddDurationUseCase {

    companion object {

        const val DURATION_REGEX = "([0-9]+[smhdwMy])+"

    }

    override fun invoke(input1: Instant, input2: String): Instant {
        var index = 0
        var result = input1
        while (index < input2.length) {
            index = input2.skipWhile(index) { it == ' ' }
            val component = input2.substringWhile(index) { it in '0'..'9' }
            index += component.length
            val unitName = input2.substringWhile(index) { it in 'a'..'z' || it in 'A'..'Z' }
            index += unitName.length
            result = result.plus(component.toLong(), unitByShortName(unitName), TimeZone.currentSystemDefault())
        }
        return result
    }

    fun unitByShortName(shortName: String): DateTimeUnit = when (shortName) {
        "s" -> DateTimeUnit.SECOND
        "m" -> DateTimeUnit.MINUTE
        "h" -> DateTimeUnit.HOUR
        "d" -> DateTimeUnit.DAY
        "w" -> DateTimeUnit.WEEK
        "M" -> DateTimeUnit.MONTH
        "y" -> DateTimeUnit.YEAR
        else -> throw IllegalArgumentException("Unknown unit short name: $shortName")
    }

    private inline fun String.skipWhile(startIndex: Int, predicate: (Char) -> Boolean): Int {
        var i = startIndex
        while (i < length && predicate(this[i])) i++
        return i
    }

    private inline fun String.substringWhile(startIndex: Int, predicate: (Char) -> Boolean): String =
        substring(startIndex, skipWhile(startIndex, predicate))

}

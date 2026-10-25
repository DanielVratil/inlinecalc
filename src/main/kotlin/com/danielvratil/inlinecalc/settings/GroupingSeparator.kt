package com.danielvratil.inlinecalc.settings

import com.danielvratil.inlinecalc.InlineCalcBundle
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Character used to group digits of the integer part of a displayed result. */
enum class GroupingSeparator(private val messageKey: String, private val char: Char?) {
    NONE("settings.grouping.none", null),
    SPACE("settings.grouping.space", ' '),
    COMMA("settings.grouping.comma", ','),
    PERIOD("settings.grouping.period", '.'),
    APOSTROPHE("settings.grouping.apostrophe", '\'');

    val displayName: String get() = InlineCalcBundle[messageKey]

    /** Formats [value] as a plain decimal number with the integer part grouped by thousands. */
    fun format(value: BigDecimal): String {
        val symbols = DecimalFormatSymbols.getInstance(Locale.ROOT)
        if (char != null) symbols.groupingSeparator = char
        val format = DecimalFormat("#,##0", symbols).apply {
            isGroupingUsed = char != null
            maximumFractionDigits = Int.MAX_VALUE
        }
        return format.format(value)
    }
}

package com.danielvratil.inlinecalc.settings

import com.danielvratil.inlinecalc.InlineCalcBundle

/** Character that can be removed from the selected text before it is evaluated. */
enum class IgnoredCharacter(private val messageKey: String, val char: Char) {
    SPACE("settings.ignored.space", ' '),
    COMMA("settings.ignored.comma", ','),
    APOSTROPHE("settings.ignored.apostrophe", '\''),
    UNDERSCORE("settings.ignored.underscore", '_');

    val displayName: String get() = InlineCalcBundle[messageKey]
}

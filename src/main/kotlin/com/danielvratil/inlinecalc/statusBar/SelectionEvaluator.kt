package com.danielvratil.inlinecalc.statusBar

import com.danielvratil.inlinecalc.services.ExpressionEvaluatorService
import com.danielvratil.inlinecalc.services.ExpressionException
import com.danielvratil.inlinecalc.settings.InlineCalcSettings
import com.intellij.openapi.components.service
import java.math.BigDecimal

/** Evaluates selected text and keeps the outcome of the latest evaluation. */
class SelectionEvaluator {

    /** Value of the last [update], or null if the text was not an expression. */
    var value: BigDecimal? = null
        private set

    /** The expression [value] was computed from, or null if there is no value. */
    private var expression: String? = null

    /** [value] formatted for display, or an empty string if there is no value. */
    val result: String
        get() {
            val value = value ?: return ""
            val settings = InlineCalcSettings.getInstance()
            val formatted = settings.resultPrefix + settings.groupingSeparator.format(value)
            return if (settings.showExpression) "$expression $formatted" else formatted
        }

    /** Evaluates [selectedText] and stores the outcome in [value]. @return true if the displayed result changed. */
    fun update(selectedText: String?): Boolean {
        val newExpression = prepare(selectedText)
        val newValue = newExpression?.let(::evaluate)
        val valueChanged = when {
            newValue == null || value == null -> newValue !== value
            else -> newValue.compareTo(value) != 0
        }
        val changed = valueChanged || (InlineCalcSettings.getInstance().showExpression && newExpression != expression)
        if (changed) {
            value = newValue
            expression = newExpression.takeIf { newValue != null }
        }
        return changed
    }

    /** Returns [selectedText] as passed to the evaluator, or null if it is empty or too long. */
    private fun prepare(selectedText: String?): String? {
        val text = selectedText?.trim()
        val settings = InlineCalcSettings.getInstance()
        if (text.isNullOrEmpty() || text.length > settings.maxExpressionLength) return null
        val ignored = settings.ignoredCharacters
        return text.filterNot { it in ignored }.trim()
    }

    private fun evaluate(expression: String): BigDecimal? = try {
        service<ExpressionEvaluatorService>().evaluate(expression)
    } catch (_: ExpressionException) {
        null
    }
}

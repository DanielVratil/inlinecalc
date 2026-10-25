package com.danielvratil.inlinecalc.services

import com.ezylang.evalex.EvaluationException
import com.ezylang.evalex.Expression
import com.ezylang.evalex.parser.ParseException
import com.intellij.openapi.components.Service
import java.math.BigDecimal

class ExpressionException(message: String) : Exception(message)

/** Evaluates numeric expressions using EvalEx. */
@Service(Service.Level.APP)
class ExpressionEvaluatorService {

    /** @throws ExpressionException if [expression] is malformed, cannot be computed or does not result in a number. */
    fun evaluate(expression: String): BigDecimal {
        if (expression.isBlank()) throw ExpressionException("Empty expression")
        val value = try {
            Expression(expression).evaluate()
        } catch (e: EvaluationException) {
            throw ExpressionException(e.message.orEmpty())
        } catch (e: ParseException) {
            throw ExpressionException(e.message.orEmpty())
        }
        if (!value.isNumberValue) throw ExpressionException("Expression does not result in a number")
        return value.numberValue
    }
}

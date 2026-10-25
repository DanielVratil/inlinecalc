package com.danielvratil.inlinecalc

import com.danielvratil.inlinecalc.services.ExpressionEvaluatorService
import com.danielvratil.inlinecalc.services.ExpressionException
import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class InlineCalcPluginTest : BasePlatformTestCase() {

    private fun eval(expression: String) = service<ExpressionEvaluatorService>().evaluate(expression).toPlainString()

    fun testArithmetic() {
        assertEquals("7", eval("1 + 2 * 3"))
        assertEquals("9", eval("(1 + 2) * 3"))
        assertEquals("2.5", eval("5 / 2"))
        assertEquals("1", eval("7 % 3"))
        assertEquals("0.3", eval("0.1 + 0.2"))
        assertEquals("100", eval("10 * 10"))
    }

    fun testUnaryAndPower() {
        assertEquals("4", eval("-2^2"))
        assertEquals("512", eval("2^3^2"))
        assertEquals("5", eval("--5"))
        assertEquals("0.25", eval("2^-2"))
        assertEquals("1.414213562373", eval("2^0.5").take(14))
    }

    fun testErrors() {
        for (bad in listOf("", "1 +", "(1", "1 / 0", "abc", "1 2", "true", "\"text\"")) {
            try {
                eval(bad)
                fail("Expected ExpressionException for '$bad'")
            } catch (_: ExpressionException) {
            }
        }
    }
}

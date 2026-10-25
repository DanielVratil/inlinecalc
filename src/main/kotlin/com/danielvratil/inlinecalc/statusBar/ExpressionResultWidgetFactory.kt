package com.danielvratil.inlinecalc.statusBar

import com.danielvratil.inlinecalc.InlineCalcBundle
import com.danielvratil.inlinecalc.settings.InlineCalcSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

internal const val WIDGET_ID = "InlineCalc.ResultWidget"

class ExpressionResultWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = WIDGET_ID
    override fun getDisplayName() = InlineCalcBundle["widget.name"]
    override fun createWidget(project: Project): StatusBarWidget = ExpressionResultWidget(project)
}

/** Status bar widget displaying the result held by [SelectionEvaluator]; clicking it copies the value. */
internal class ExpressionResultWidget(private val project: Project) : CustomStatusBarWidget {

    private val evaluator = SelectionEvaluator()
    private val label = ResultLabel { evaluator.value?.toPlainString() }

    override fun ID() = WIDGET_ID

    override fun install(statusBar: StatusBar) {
        SelectionSource.EP_NAME.extensionList.forEach { it.install(project, this, ::onSelected) }
    }

    /** Evaluates [text], or clears the result if the plugin is disabled, and updates the label if the result changed. */
    private fun onSelected(text: String?) {
        val enabled = InlineCalcSettings.getInstance().enabled
        if (evaluator.update(text.takeIf { enabled })) label.setResult(evaluator.result)
    }

    override fun getComponent() = label

    val hasResult: Boolean get() = InlineCalcSettings.getInstance().enabled && evaluator.value != null

    /** Copies the current result, as if the label was clicked. */
    fun copyResult() {
        if (hasResult) label.copy()
    }

    override fun dispose() {}
}

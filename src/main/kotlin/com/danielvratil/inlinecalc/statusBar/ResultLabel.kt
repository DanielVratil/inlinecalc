package com.danielvratil.inlinecalc.statusBar

import com.danielvratil.inlinecalc.InlineCalcBundle
import com.intellij.ide.IdeTooltip
import com.intellij.ide.IdeTooltipManager
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import java.awt.Cursor
import java.awt.Point
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 * Label showing an expression result. Clicking copies the text from [valueProvider] to the clipboard
 * and switches the tooltip to the copied value.
 *
 * @param valueProvider returns the text to copy, or null if there is nothing to copy.
 */
class ResultLabel(private val valueProvider: () -> String?) : JBLabel() {

    init {
        border = JBUI.Borders.empty(0, 6)
        isVisible = false

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            }

            override fun mouseExited(e: MouseEvent) {
                toolTipText = InlineCalcBundle["widget.tooltip"]
            }

            override fun mouseClicked(e: MouseEvent) = copy(e.point)
        })
    }

    /** Copies the value from [valueProvider] and shows a tooltip with it at [point]. */
    fun copy(point: Point = Point(width / 2, height / 2)) {
        val copied = valueProvider() ?: return
        CopyPasteManager.getInstance().setContents(StringSelection(copied))
        val message = InlineCalcBundle["widget.copied", copied]
        toolTipText = message
        // The press that started a click has already dismissed the regular tooltip, so show a new one
        val tooltip = IdeTooltip(this, point, JBLabel(message))
            .setPreferredPosition(Balloon.Position.above)
        IdeTooltipManager.getInstance().show(tooltip, true)
    }

    /** Shows [text], or hides the label if it is empty. */
    fun setResult(text: String) {
        this.text = text
        toolTipText = InlineCalcBundle["widget.tooltip"]
        isVisible = text.isNotEmpty()
    }
}

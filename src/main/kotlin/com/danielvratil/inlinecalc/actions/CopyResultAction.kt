package com.danielvratil.inlinecalc.actions

import com.danielvratil.inlinecalc.statusBar.ExpressionResultWidget
import com.danielvratil.inlinecalc.statusBar.WIDGET_ID
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager

/** Copies the result shown in the status bar widget. Has no default shortcut; it can be assigned in Keymap. */
class CopyResultAction : AnAction(), DumbAware {

    override fun getActionUpdateThread() = ActionUpdateThread.EDT

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = event.project?.let(::widget)?.hasResult == true
    }

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let(::widget)?.copyResult()
    }

    private fun widget(project: Project) =
        WindowManager.getInstance().getStatusBar(project)?.getWidget(WIDGET_ID) as? ExpressionResultWidget
}

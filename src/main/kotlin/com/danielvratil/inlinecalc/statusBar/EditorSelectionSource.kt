package com.danielvratil.inlinecalc.statusBar

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.SelectionEvent
import com.intellij.openapi.editor.event.SelectionListener
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project

/** Reports the text selected in editors, and the selection of the editor that becomes active. */
class EditorSelectionSource : SelectionSource {

    override fun install(project: Project, parent: Disposable, onSelected: (String?) -> Unit) {
        EditorFactory.getInstance().eventMulticaster.addSelectionListener(object : SelectionListener {
            override fun selectionChanged(e: SelectionEvent) {
                if (e.editor.project == project) onSelected(selectedText(e.editor))
            }
        }, parent)
        project.messageBus.connect(parent).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, object : FileEditorManagerListener {
            override fun selectionChanged(event: FileEditorManagerEvent) {
                onSelected(selectedText(event.manager.selectedTextEditor))
            }
        })
    }

    private fun selectedText(editor: Editor?) =
        editor?.caretModel?.allCarets?.joinToString(" ") { it.selectedText ?: "" }
}

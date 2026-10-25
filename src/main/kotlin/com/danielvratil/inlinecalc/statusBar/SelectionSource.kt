package com.danielvratil.inlinecalc.statusBar

import com.intellij.openapi.Disposable
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project

/** Source of selected text, e.g. text editors or data grids. Optional sources are contributed by optional modules. */
interface SelectionSource {

    /**
     * Starts reporting selections in [project] to [onSelected] until [parent] is disposed.
     * The text is null if the selection has no text that can be evaluated.
     */
    fun install(project: Project, parent: Disposable, onSelected: (String?) -> Unit)

    companion object {
        val EP_NAME = ExtensionPointName.create<SelectionSource>("com.danielvratil.inlinecalc.selectionSource")
    }
}

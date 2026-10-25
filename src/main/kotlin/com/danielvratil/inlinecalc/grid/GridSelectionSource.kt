package com.danielvratil.inlinecalc.grid

import com.danielvratil.inlinecalc.statusBar.SelectionSource
import com.intellij.database.datagrid.DataGrid
import com.intellij.database.datagrid.DataGridListener
import com.intellij.database.run.ui.DataAccessType
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

/**
 * Reports the value of the selected cell in a data grid (e.g. the table view of a CSV file) as selected text.
 * Grid selections do not produce editor selection events. Nothing is reported for selections of several cells.
 *
 * Registered in `inlinecalc-withGrid.xml`, which is only loaded when the Grid Core plugin is available.
 */
class GridSelectionSource : SelectionSource {

    override fun install(project: Project, parent: Disposable, onSelected: (String?) -> Unit) {
        project.messageBus.connect(parent).subscribe(DataGridListener.TOPIC, object : DataGridListener {
            override fun onSelectionChanged(grid: DataGrid) = onSelected(selectedCellText(grid))

            override fun onSelectionChanged(grid: DataGrid, fromUser: Boolean) =
                onSelected(selectedCellText(grid))
        })
    }

    private fun selectedCellText(grid: DataGrid): String? {
        val selection = grid.selectionModel
        if (selection.selectedRowCount != 1 || selection.selectedColumnCount != 1) return null
        val value = grid.getDataModel(DataAccessType.DATA_WITH_MUTATIONS)
            .getValueAt(selection.selectedRow, selection.selectedColumn)
        return if (value is String || value is Number) value.toString() else null
    }
}

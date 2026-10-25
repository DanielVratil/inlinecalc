package com.danielvratil.inlinecalc.settings

import com.danielvratil.inlinecalc.InlineCalcBundle
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.listCellRenderer.textListCellRenderer

/** Settings page under Settings | Tools | Inline Calc. */
class InlineCalcConfigurable : BoundSearchableConfigurable(
    InlineCalcBundle["settings.display.name"],
    "com.danielvratil.inlinecalc.settings",
) {

    override fun createPanel(): DialogPanel {
        val settings = InlineCalcSettings.getInstance()
        return panel {
            row {
                checkBox(InlineCalcBundle["settings.enabled"]).bindSelected(settings::enabled)
            }
            group(InlineCalcBundle["settings.output"]) {
                row(InlineCalcBundle["settings.grouping"]) {
                    comboBox(GroupingSeparator.entries, textListCellRenderer { it?.displayName })
                        .bindItem(
                            { settings.groupingSeparator },
                            { settings.groupingSeparator = it ?: GroupingSeparator.NONE },
                        )
                }
                row(InlineCalcBundle["settings.prefix"]) {
                    textField().bindText(settings::resultPrefix)
                }
                row {
                    checkBox(InlineCalcBundle["settings.showExpression"]).bindSelected(settings::showExpression)
                }
            }
            group(InlineCalcBundle["settings.input"]) {
                row(InlineCalcBundle["settings.maxLength"]) {
                    intTextField(1..1_000_000).bindIntText(settings::maxExpressionLength)
                }
                row(InlineCalcBundle["settings.ignored"]) {}
                for (character in IgnoredCharacter.entries) {
                    row {
                        checkBox(character.displayName).bindSelected(
                            { settings.isIgnored(character) },
                            { settings.setIgnored(character, it) },
                        )
                    }
                }
            }
        }
    }
}

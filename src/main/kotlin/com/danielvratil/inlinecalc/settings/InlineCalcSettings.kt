package com.danielvratil.inlinecalc.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

/** Application-level plugin settings, persisted in `InlineCalc.xml`. */
@Service(Service.Level.APP)
@State(name = "InlineCalcSettings", storages = [Storage("InlineCalc.xml")])
class InlineCalcSettings : PersistentStateComponent<InlineCalcSettings.State> {

    class State {
        var enabled: Boolean = true
        var groupingSeparator: GroupingSeparator = GroupingSeparator.NONE
        var resultPrefix: String = "= "
        var showExpression: Boolean = false
        var maxExpressionLength: Int = 300

        /** Names of [IgnoredCharacter] entries; stored as strings to keep the XML stable if the enum changes. */
        var ignoredCharacters: MutableSet<String> = mutableSetOf()
    }

    private var state = State()

    var enabled: Boolean
        get() = state.enabled
        set(value) {
            state.enabled = value
        }

    var groupingSeparator: GroupingSeparator
        get() = state.groupingSeparator
        set(value) {
            state.groupingSeparator = value
        }

    var resultPrefix: String
        get() = state.resultPrefix
        set(value) {
            state.resultPrefix = value
        }

    var showExpression: Boolean
        get() = state.showExpression
        set(value) {
            state.showExpression = value
        }

    var maxExpressionLength: Int
        get() = state.maxExpressionLength
        set(value) {
            state.maxExpressionLength = value
        }

    /** Characters removed from expressions before evaluation. */
    val ignoredCharacters: Set<Char>
        get() = IgnoredCharacter.entries.filter { it.name in state.ignoredCharacters }.mapTo(mutableSetOf()) { it.char }

    fun isIgnored(character: IgnoredCharacter) = character.name in state.ignoredCharacters

    fun setIgnored(character: IgnoredCharacter, ignored: Boolean) {
        if (ignored) state.ignoredCharacters.add(character.name) else state.ignoredCharacters.remove(character.name)
    }

    override fun getState() = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): InlineCalcSettings = service()
    }
}

package linter.rules.abs

import kotlinx.serialization.json.JsonElement

interface RuleSettings {
    fun setRule(options: Map<String, JsonElement>)
}

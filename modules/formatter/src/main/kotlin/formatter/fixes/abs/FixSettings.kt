package formatter.fixes.abs

import kotlinx.serialization.json.JsonElement

interface FixSettings {
    fun setFix(fixes: Map<String, JsonElement>)
}

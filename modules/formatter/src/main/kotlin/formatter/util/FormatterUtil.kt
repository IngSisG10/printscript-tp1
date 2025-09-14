package formatter.util

import formatter.Formatter
import formatter.fixes.abs.FixSettings
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Serializable
data class Config(
    val options: Map<String, JsonElement>,
)

class FormatterUtil {
    companion object {
        private fun addVersionFixes(version: String): List<FormatterFix> {
            val onePointZeroFormatFixes =
                emptyList<FormatterFix>()
            // todo: add fixes for 1.0

            val onePointOneFormatFixes =
                emptyList<FormatterFix>()
            // todo: add fixes for 1.0 && 1.1

            return when (version) {
                "1.1" -> onePointZeroFormatFixes
                else -> onePointOneFormatFixes
            }
        }

        private fun addFormatterFixes(
            fixesIWantToApply: Map<String, JsonElement>,
            fixes: List<FormatterFix>,
        ): List<FormatterFix> {
            val appliedRules = mutableListOf<FormatterFix>()
            for (fix in fixes) {
                if (fix.applies(fixesIWantToApply)) {
                    if (fix is FixSettings) {
                        fix.setFix(fixesIWantToApply)
                    }
                    appliedRules.add(fix)
                }
            }
            return appliedRules
        }

        fun createFormatter(
            configText: String,
            version: String,
        ): formatter.Formatter {
            val config = Json.decodeFromString<Config>(configText)
            return Formatter(
                formatterFixes = addFormatterFixes(config.options, addVersionFixes(version)),
            )
        }
    }
}

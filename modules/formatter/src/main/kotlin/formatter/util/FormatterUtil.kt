package formatter.util

import formatter.Formatter
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Config(
    val fix: List<String>,
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
            fixes: List<String>,
            possibleFixes: List<FormatterFix>,
        ): List<FormatterFix> {
            val appliedRules = mutableListOf<FormatterFix>()
            for (fix in fixes) {
                for (formatterFix in possibleFixes) {
                    if (formatterFix.getName() == fix) {
                        appliedRules.add(formatterFix)
                    }
                }
            }
            return appliedRules
        }

        fun createFormatter(
            configText: String,
            version: String = "1.0",
        ): Formatter {
            val config = Json.decodeFromString<Config>(configText)
            return Formatter(
                formatterFixes = addFormatterFixes(config.fix, addVersionFixes(version)),
            )
        }
    }
}

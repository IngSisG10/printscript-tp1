package formatter.util

import formatter.Formatter
import formatter.fixes.abs.FixSettings
import formatter.fixes.abs.FormatterFix
import formatter.fixes.custom.MaxOneBlankLineFix
import formatter.fixes.required.IfBracePlacementFix
import formatter.fixes.required.IfInnerIndentationFix
import formatter.fixes.required.LineJumpAfterSemiColonFix
import formatter.fixes.required.LineJumpSpaceBeforePrintlnFix
import formatter.fixes.required.OneSpaceAfterTokenMaxFix
import formatter.fixes.required.SpaceAfterColonFix
import formatter.fixes.required.SpaceAfterEqualFix
import formatter.fixes.required.SpaceAfterOperatorFix
import formatter.fixes.required.SpaceBeforeAndAfterEqualFix
import formatter.fixes.required.SpaceBeforeAndAfterOperatorFix
import formatter.fixes.required.SpaceBeforeColonFix
import formatter.fixes.required.SpaceBeforeEqualFix
import formatter.fixes.required.SpaceBeforeOperatorFix
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
                listOf<FormatterFix>(
                    // Basic spacing fixes
                    SpaceBeforeColonFix(),
                    SpaceAfterColonFix(),
                    OneSpaceAfterTokenMaxFix(),
                    SpaceBeforeAndAfterEqualFix(),
                    SpaceBeforeAndAfterOperatorFix(),
                    LineJumpAfterSemiColonFix(),
                    LineJumpSpaceBeforePrintlnFix(),
                    SpaceAfterEqualFix(),
                    SpaceAfterOperatorFix(),
                    SpaceBeforeEqualFix(),
                    SpaceBeforeOperatorFix(),
                    MaxOneBlankLineFix(),
                )

            val onePointOneFormatFixes =
                onePointZeroFormatFixes +
                    listOf<FormatterFix>(
                        IfBracePlacementFix(),
                        IfInnerIndentationFix(),
                    )

            return when (version) {
                "1.0" -> onePointZeroFormatFixes
                "1.1" -> onePointOneFormatFixes
                else -> onePointOneFormatFixes // Default to latest version
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
            version: String = "1.0",
        ): Formatter {
            val config = Json.decodeFromString<Config>(configText)
            return Formatter(
                formatterFixes = addFormatterFixes(config.options, addVersionFixes(version)),
            )
        }
    }
}

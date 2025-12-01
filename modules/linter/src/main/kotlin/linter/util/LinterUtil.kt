package linter.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import linter.Linter
import linter.dto.LinterDto
import linter.rules.abs.LinterRule
import linter.rules.abs.RuleSettings
import linter.rules.custom.LineJumpAfterSemicolonRule
import linter.rules.custom.NewLineBeforePrintlnRule
import linter.rules.custom.OneSpaceBetweenTokensRule
import linter.rules.custom.SpaceAfterAssignationRule
import linter.rules.custom.SpaceAfterColonRule
import linter.rules.custom.SpaceAfterOperatorRule
import linter.rules.custom.SpaceBeforeAssignationRule
import linter.rules.custom.SpaceBeforeColonRule
import linter.rules.custom.SpaceBeforeOperatorRule
import linter.rules.required.CamelCaseRule
import linter.rules.required.PascalCaseRule
import linter.rules.required.PrintLnSimpleArgumentRule
import linter.rules.required.ReadInputSimpleArgumentRule
import linter.rules.required.SnakeCaseRule

@Serializable
data class Config(
    val options: Map<String, JsonElement>,
)

class LinterUtil {
    companion object {
        private fun addVersionRules(version: String): List<LinterRule> {
            val onePointZeroLinterRules =
                listOf(
                    // required
                    CamelCaseRule(),
                    PascalCaseRule(),
                    SnakeCaseRule(),
                    PrintLnSimpleArgumentRule(),
                    // custom
                    SpaceAfterColonRule(),
                    SpaceBeforeColonRule(),
                    LineJumpAfterSemicolonRule(),
                    NewLineBeforePrintlnRule(),
                    OneSpaceBetweenTokensRule(),
                    SpaceAfterAssignationRule(),
                    SpaceBeforeAssignationRule(),
                    SpaceAfterOperatorRule(),
                    SpaceBeforeOperatorRule(),
                )
            val onePointOneLinterRules =
                listOf(
                    CamelCaseRule(),
                    PascalCaseRule(),
                    SnakeCaseRule(),
                    PrintLnSimpleArgumentRule(),
                    ReadInputSimpleArgumentRule(),
                    // custom
                    SpaceAfterColonRule(),
                    SpaceBeforeColonRule(),
                    LineJumpAfterSemicolonRule(),
                    NewLineBeforePrintlnRule(),
                    OneSpaceBetweenTokensRule(),
                    SpaceAfterAssignationRule(),
                    SpaceBeforeAssignationRule(),
                    SpaceAfterOperatorRule(),
                    SpaceBeforeOperatorRule(),
                )
            return when (version) {
                "1.1" -> onePointOneLinterRules
                else -> onePointZeroLinterRules
            }
        }

        private fun addLinterRules(
            rulesIWantToApply: Map<String, JsonElement>,
            possibleRules: List<LinterRule>,
        ): List<LinterRule> {
            val appliedRules = mutableListOf<LinterRule>()
            for (rule in possibleRules) {
                if (rule.applies(rulesIWantToApply)) {
                    if (rule is RuleSettings) {
                        rule.setRule(rulesIWantToApply)
                    }
                    appliedRules.add(rule)
                }
            }
            return appliedRules
        }

        fun createLinter(
            str: String,
            version: String = "1.0",
        ): Linter {
            val json =
                Json {
                    ignoreUnknownKeys = true
                }

            val options = json.decodeFromString<Map<String, JsonElement>>(str)

            val config = Config(options = options)

            return Linter(
                linterRules = addLinterRules(config.options, addVersionRules(version)),
            )
        }

        fun getLinterRulesData(version: String = "1.0"): List<LinterDto> = addVersionRules(version).map { it.getRuleNameAndValue() }
    }
}

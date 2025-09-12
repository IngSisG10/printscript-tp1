package linter.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import linter.Linter
import linter.rules.abs.LinterRule
import linter.rules.required.CamelCaseRule
import linter.rules.custom.LineJumpAfterSemicolonRule
import linter.rules.custom.NewLineBeforePrintlnRule
import linter.rules.custom.OneSpaceBetweenTokensRule
import linter.rules.required.PascalCaseRule
import linter.rules.required.SnakeCaseRule
import linter.rules.custom.SpaceAfterAssignationRule
import linter.rules.custom.SpaceAfterColonRule
import linter.rules.custom.SpaceAfterOperatorRule
import linter.rules.custom.SpaceBeforeAssignationRule
import linter.rules.custom.SpaceBeforeColonRule
import linter.rules.custom.SpaceBeforeOperatorRule
import linter.rules.required.PrintLnSimpleArgumentRule
import linter.rules.required.ReadInputSimpleArgumentRule

@Serializable
data class Config(
    val rules: List<String>,
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
                    SpaceBeforeOperatorRule()
                )
            val onePointOneLinterRules =
                listOf(
                    CamelCaseRule(),
                    PascalCaseRule(),
                    SnakeCaseRule(),
                    SpaceAfterColonRule(),
                    SpaceBeforeColonRule(),
                    LineJumpAfterSemicolonRule(),
                    NewLineBeforePrintlnRule(),
                    OneSpaceBetweenTokensRule(),
                    SpaceAfterAssignationRule(),
                    SpaceBeforeAssignationRule(),
                    SpaceAfterOperatorRule(),
                    SpaceBeforeOperatorRule(),
                    ReadInputSimpleArgumentRule(),
                )
            return when (version) {
                "1.1" -> onePointOneLinterRules
                else -> onePointZeroLinterRules
            }
        }

        // TODO: implement versions for linter
        private fun addLinterRules(
            rules: List<String>,
            possibleRules: List<LinterRule>,
        ): List<LinterRule> {
            val appliedRules = mutableListOf<LinterRule>()
            for (rule in rules) {
                for (linterRule in possibleRules) {
                    if (linterRule.getName() == rule) {
                        appliedRules.add(linterRule)
                    }
                }
            }
            return appliedRules
        }

        fun createLinter(
            str: String,
            version: String = "1.0",
        ): Linter {
            val config = Json.decodeFromString<Config>(str)
            return Linter(
                linterRules = addLinterRules(config.rules, addVersionRules(version)),
            )
        }
    }
}

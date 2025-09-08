package linter.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import linter.Linter
import linter.syntax.LinterRule
import linter.syntax.rules.CamelCaseRule
import linter.syntax.rules.LineJumpAfterSemicolonRule
import linter.syntax.rules.NewLineBeforePrintlnRule
import linter.syntax.rules.OneSpaceBetweenTokensRule
import linter.syntax.rules.PascalCaseRule
import linter.syntax.rules.SnakeCaseRule
import linter.syntax.rules.SpaceAfterAssignationRule
import linter.syntax.rules.SpaceAfterColonRule
import linter.syntax.rules.SpaceAfterOperatorRule
import linter.syntax.rules.SpaceBeforeAssignationRule
import linter.syntax.rules.SpaceBeforeColonRule
import linter.syntax.rules.SpaceBeforeOperatorRule

@Serializable
data class Config(
    val rules: List<String>,
)

interface LinterUtil {
    fun createLinter(str: String): Linter {
        val config = Json.decodeFromString<Config>(str)
        return Linter(
            linterRules = addLinterRules(config.rules),
        )
    }

    // TODO: implement versions for linter
    private fun addLinterRules(
        rules: List<String>,
        version: String = "1.0",
    ): List<LinterRule> {
        val onePointZeroLinterRules =
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
            )
        val appliedRules = mutableListOf<LinterRule>()
        for (rule in rules) {
            for (linterRule in onePointZeroLinterRules) {
                if (linterRule.getName() == rule) {
                    appliedRules.add(linterRule)
                }
            }
        }
        return appliedRules
    }
}

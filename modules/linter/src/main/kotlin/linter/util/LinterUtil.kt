package linter.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import linter.Linter
import linter.syntax.LinterRule
import linter.syntax.rules.CamelCaseRule
import linter.syntax.rules.PascalCaseRule
import linter.syntax.rules.SnakeCaseRule
import linter.syntax.rules.SpaceAfterColonRule
import linter.syntax.rules.SpaceBeforeColonRule

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

    private fun addLinterRules(rules: List<String>): List<LinterRule> {
        val linterRules =
            listOf(
                CamelCaseRule(),
                PascalCaseRule(),
                SnakeCaseRule(),
                SpaceAfterColonRule(),
                SpaceBeforeColonRule(),
                // TODO: implement new rules here
            )
        val appliedRules = mutableListOf<LinterRule>()
        for (rule in rules) {
            for (linterRule in linterRules) {
                if (linterRule.getName() == rule) {
                    appliedRules.add(linterRule)
                }
            }
        }
        return appliedRules
    }
}

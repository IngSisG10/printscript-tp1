package helper.util

import Linter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import lexer.Lexer
import lexer.token.TokenRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule
import syntax.LinterRule
import syntax.rules.CamelCaseRule
import syntax.rules.PascalCaseRule
import syntax.rules.SnakeCaseRule
import syntax.rules.SpaceAfterColonRule
import syntax.rules.SpaceBeforeColonRule
import java.io.File

@Serializable
data class Config(
    val rules: List<String>,
)

interface CliUtil {
    fun findFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("file '$filename' not found")
            return null
        }

        return file.readText()
    }

    fun createLexer(version: String): Lexer {
        val versionOnePointOne =
            listOf<TokenRule>(
                StringLiteralRule(),
                NumberLiteralRule(),
                KeywordRule(),
                ParenthesisRule(),
                SingleCharRule(),
                IdentifierRule(),
                // TODO: add new features here
            )

        return when (version) {
            "1.1" -> Lexer(versionOnePointOne)
            "1.0" -> Lexer() // 1.0 is default
            else -> throw Exception("Unsupported version")
        }
    }

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

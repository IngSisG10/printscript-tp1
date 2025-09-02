package helper.commands

import Linter
import exception.InvalidFileException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import lexer.Lexer
import syntax.LinterRule
import syntax.rules.CamelCaseRule
import syntax.rules.PascalCaseRule
import syntax.rules.SnakeCaseRule
import syntax.rules.SpaceAfterColonRule
import syntax.rules.SpaceBeforeColonRule

@Serializable
data class Config(
    val rules: List<String>,
)

class AnalyzingCommand : CliCommand {
    override fun tryRun(args: Array<String>) {
        if (args[0] != "analyze") return
        val fileText = tryFindFile(args[1]) ?: throw InvalidFileException("No file was found")
        val configFileText = tryFindFile(args[2]) ?: throw InvalidFileException("No config file was found")
        val linter = addConfig(configFileText)
        val lexer = Lexer(fileText)
        val tokens = lexer.lex()
        linter.lint(tokens)
        println("Code is in order")
    }

    private fun addConfig(configFileText: String): Linter {
        val config = Json.decodeFromString<Config>(configFileText)
        return Linter(
            linterRules = addLinterRules(config.rules),
        )
    }

    private fun addLinterRules(rules: List<String>): List<LinterRule> {
        val linterRules = mutableListOf<LinterRule>()
        for (rule in rules) {
            when (rule) {
                "CamelCase" -> linterRules.add(CamelCaseRule())
                "PascalCase" -> linterRules.add(PascalCaseRule())
                "SnakeCaseRule" -> linterRules.add(SnakeCaseRule())
                "SpaceAfterColon" -> linterRules.add(SpaceAfterColonRule())
                "SpaceBeforeColon" -> linterRules.add(SpaceBeforeColonRule())
            }
        }
        return linterRules
    }
}

package helper.commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import exception.InvalidFileException
import helper.util.CliUtil
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import syntax.LinterRule
import syntax.rules.CamelCaseRule
import syntax.rules.PascalCaseRule
import syntax.rules.SnakeCaseRule
import syntax.rules.SpaceAfterColonRule
import syntax.rules.SpaceBeforeColonRule

class Analyze :
    CliktCommand(),
    CliUtil {
    private val file by argument()
    private val config by argument()
    private val version by argument().default("1.0")

    private val fileText = findFile(file) ?: throw throw InvalidFileException("No file was found")
    private val configFileText = findFile(config) ?: throw throw InvalidFileException("No file was found")

    override fun run() {
        val linter = addConfig(configFileText)
        val lexer = createLexer(version)
        val tokens = lexer.lex(fileText)
        linter.lint(tokens)
        println("Code is in order")
    }

    @Serializable
    data class Config(
        val rules: List<String>,
    )

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

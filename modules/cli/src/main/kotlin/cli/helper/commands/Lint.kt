package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import lexer.util.LexerUtil.Companion.createLexer

class Lint :
    CliktCommand(),
    CliUtil {
    private val file by argument()
    private val config by argument()
    private val version by option(
        "-v",
        "--version",
        help = "The running version of your code",
    ).default("1.0")

    override fun run() {
        val fileText = findFile(file) ?: throw throw common.exception.InvalidFileException("No file was found")
        val configFileText = findFile(config) ?: throw throw common.exception.InvalidFileException("No file was found")
        val linter = createLinter(configFileText)
        val lexer = createLexer(version)
        val tokens = lexer.lex(fileText)
        linter.lint(tokens)
        println("Code is in order")
    }
}

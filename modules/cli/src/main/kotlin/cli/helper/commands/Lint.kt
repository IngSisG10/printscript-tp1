package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import lexer.util.LexerUtil.Companion.createLexer
import linter.util.LinterUtil.Companion.createLinter

class Lint : CliktCommand() {
    private val file by argument()
    private val config by argument()
    private val version by option(
        "-v",
        "--version",
        help = "The running version of your code",
    ).default("1.0")

    override fun run() {
        val fileText = CliUtil.findFile(file) ?: throw throw common.exception.InvalidFileException("No file was found")
        val configFileText = CliUtil.findFile(config) ?: throw throw common.exception.InvalidFileException("No file was found")
        val linter = createLinter(configFileText)
        val lexer = createLexer(version)
        val tokens = lexer.lex(fileText)
        linter.lint(tokens)
        println("Code is in order")
    }
}

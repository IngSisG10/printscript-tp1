package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import formatter.Formatter
import lexer.util.LexerUtil.Companion.createLexer
import linter.util.LinterUtil.Companion.createLinter

class Format : CliktCommand() {
    private val file by argument()
    private val config by argument()
    private val version by option(
        "-v",
        "--version",
        help = "The running version of your code",
    ).default("1.0")

    override fun run() {
        val code = CliUtil.findFile(file) ?: throw common.exception.InvalidFileException()
        val lexer = createLexer(version)
        val tokens = lexer.lex(code)
        val configText = CliUtil.findFile(config) ?: throw common.exception.InvalidFileException()
        val linter = createLinter(configText)
        val formatter = Formatter(linter)
        val formattedCode = formatter.format(tokens)
        println(formattedCode)
    }
}

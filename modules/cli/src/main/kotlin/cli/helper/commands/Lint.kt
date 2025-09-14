package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import common.util.segmentsBySemicolon
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
        val lexer = createLexer(version)
        val linter = createLinter(config)
        val fileText = CliUtil.findFile(file) ?: throw common.exception.InvalidFileException("No file was found")
        val inputStream = fileText.byteInputStream()
        inputStream.segmentsBySemicolon().forEach { segment ->
            try {
                val tokens = lexer.lex(segment)
                val lintErrors = linter.lint(tokens)
            } catch (t: Throwable) {
                throw t
            }
        }
    }
}

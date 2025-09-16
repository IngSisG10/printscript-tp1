package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import common.util.segmentsBySemicolonPreserveWhitespace
import formatter.util.FormatterUtil
import lexer.util.LexerUtil.Companion.createLexer

class Format : CliktCommand() {
    private val file by argument()
    private val config by argument()
    private val version by option(
        "-v",
        "--version",
        help = "The running version of your code",
    ).default("1.0")

    override fun run() {
        val lexer = createLexer(version)
        val configText =
            CliUtil.findFile(config)
                ?: throw common.exception.InvalidFileException("No file was found")
        val formatter = FormatterUtil.createFormatter(configText, version)
        val fileText =
            CliUtil.findFile(file)
                ?: throw common.exception.InvalidFileException("No file was found")
        val inputStream = fileText.byteInputStream()
        inputStream.segmentsBySemicolonPreserveWhitespace().forEach { segment ->
            try {
                val tokens = lexer.lex(segment)
                val formatterText = formatter.format(tokens)
                terminal.println(formatterText)
            } catch (t: Throwable) {
                currentContext.fail(t.message ?: t.toString())
            }
        }
    }
}

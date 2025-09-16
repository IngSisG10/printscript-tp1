package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import common.exception.InvalidFileException
import common.util.segmentsBySemicolon
import interpreter.Interpreter
import lexer.util.LexerUtil.Companion.createLexer
import parser.Parser

class Execute : CliktCommand() {
    private val fileName by argument()
    private val version by option(
        "-v",
        "--version",
        help = "The running version of your code",
    ).default("1.0")

    override fun help(context: Context) = "Execute the desired file"

    override fun run() {
        try {
            val fileText = CliUtil.findFile(fileName) ?: throw InvalidFileException("No file was found")
            val lexer = createLexer(version)
            val parser = Parser()
            val interpreter = Interpreter()
            val inputStream = fileText.byteInputStream()
            inputStream.segmentsBySemicolon().forEach { segment ->
                val tokens = lexer.lex(segment)
                val ast = parser.parse(tokens)
                val output = interpreter.interpret(ast)
                output.forEach { line ->
                    terminal.println(line)
                }
            }
        } catch (t: Throwable) {
            currentContext.fail(t.message ?: t.toString())
        }
    }
}

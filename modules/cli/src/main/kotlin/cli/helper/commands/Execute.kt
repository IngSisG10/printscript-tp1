package cli.helper.commands

import cli.helper.util.CliUtil
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import common.exception.InvalidFileException
import interpreter.PrintScriptInterpreter
import parser.Parser

class Execute :
    CliktCommand(),
    CliUtil {
    private val fileName by argument()
    private val version by option(
        "-v",
        "--version",
        help = "The running version of your code",
    ).default("1.0")

    override fun help(context: Context) = "Execute the desired file"

    override fun run() {
        val fileText = findFile(fileName) ?: throw throw common.exception.InvalidFileException("No file was found")
        val lexer = createLexer(version)
        val tokens = lexer.lex(fileText)
        val parser = Parser()
        val ast = parser.parse(tokens)
        val interpreter = PrintScriptInterpreter()
        val output = interpreter.interpret(ast)
        for (line in output) println(line)
    }
}

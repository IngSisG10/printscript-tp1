package helper.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import exception.InvalidFileException
import helper.util.CliUtil
import interpreter.PrintScriptInterpreter
import lexer.Lexer
import parser.Parser

class Execute :
    CliktCommand(),
    CliUtil {
    private val fileName by argument()
    private val fileText = findFile(fileName) ?: throw throw InvalidFileException("No file was found")
    private val version by argument().default("1.1")

    override fun help(context: Context) = "Execute the desired file"

    override fun run() {
        val lexer = Lexer(fileText)
        val tokens = lexer.lex()
        val parser = Parser(tokens)
        val ast = parser.parse()
        val interpreter = PrintScriptInterpreter()
        val output = interpreter.interpret(ast)
        for (line in output) println(line)
    }
}

package helper.commands

import Formatter
import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import exception.InvalidFileException
import helper.util.CliUtil

class Format :
    CoreCliktCommand(),
    CliUtil {
    private val file by argument()
    private val config by argument()
    private val version by argument().default("1.0")

    override fun run() {
        val code = findFile(file) ?: throw InvalidFileException()
        val lexer = createLexer(version)
        val tokens = lexer.lex(code)
        val configText = findFile(config) ?: throw InvalidFileException()
        val linter = createLinter(configText)
        val formatter = Formatter(linter)
        val formattedCode = formatter.format(tokens)
        println(formattedCode)
    }
}

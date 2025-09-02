package helper.commands

import exception.InvalidFileException
import interpreter.PrintScriptInterpreter
import lexer.Lexer
import parser.Parser
import java.io.File

class ExecutionCommand : CliCommand {
    override fun tryRun(args: Array<String>) {
        if (args.isEmpty()) return
        if (args[0] != "Execution") return
        val fileText = tryFindFile(args[1]) ?: throw InvalidFileException("No file was found")
        println(fileText)
        val lexer = Lexer(fileText)
        val tokens = lexer.lex()
        val parser = Parser(tokens)
        val ast = parser.parse()
        val interpreter = PrintScriptInterpreter()
        val output = interpreter.interpret(ast)
        for (line in output) println(line)
    }

    private fun tryFindFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("El archivo '$filename' no existe")
            return null
        }

        return file.readText()
    }
}

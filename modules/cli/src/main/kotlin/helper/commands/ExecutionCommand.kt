package helper.commands

import exception.InvalidFileException
import interpreter.PrintScriptInterpreter
import lexer.Lexer
import parser.Parser

class ExecutionCommand : CliCommand {
    override fun tryRun(args: Array<String>) {
        if (args.isEmpty()) return
        if (args[0] != "execute") return
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
}

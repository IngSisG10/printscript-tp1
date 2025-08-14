import lexer.Lexer
import parser.Parser
import interpreter.Interpreter

fun main(args: Array<String>) {
    val lexer = Lexer("println(\"hello world\")")
    val tokens = lexer.lex()
    val parser = Parser(tokens)
    println(tokens)
    parser.parse()

    wihidauhwadwhiuadwhiu
}
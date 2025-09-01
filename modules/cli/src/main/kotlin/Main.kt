import helper.Cli
import lexer.Lexer
import parser.Parser

fun main(args: Array<String>) {
    val cli = Cli()
    cli.execute(args)

    val lexer = Lexer("let a : Number = 5 + 5;")
    val tokens = lexer.lex()
    val parser = Parser(tokens)
    val ast = parser.parse()
}

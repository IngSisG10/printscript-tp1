import helper.Cli

fun main(args: Array<String>) {
    val cli = Cli()
    cli.execute(args)

//    val lexer = Lexer("let a : Number = 5 + 5;")
//    val tokens = lexer.lex()
//    val parser = Parser(tokens)
//    val ast = parser.parse()
}

import lexer.Lexer
import parser.Parser

fun main(args: Array<String>) {
    // val lexer = Lexer("println(\"hello world\")")
    // val lexer = Lexer("let a: Number = 5;")
    // val lexer = Lexer("let a: Number = 5 + 5")
    val lexer = Lexer("let a : Number = \"5\" + 5;")
    val tokens = lexer.lex()
    val parser = Parser(tokens)
    println(tokens)
    val ast = parser.parse() // todo: deberia tirar SemanticError, pero no lo hace.
    println("\nAST Nodes:")
    ast.forEachIndexed { index, node ->
        println("  [$index] ${node.javaClass.simpleName}")
        PrettyPrinter.printASTTree(node, "    ")
    }
}

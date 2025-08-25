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
        printASTTree(node, "    ")
    }
}

fun printASTTree(
    node: Any,
    indent: String = "",
) {
    when (node.javaClass.simpleName) {
        "DeclaratorNode" -> {
            try {
                println("$indent├─ DeclaratorNode")

                val variableNodeField = node.javaClass.getDeclaredField("variableNode")
                variableNodeField.isAccessible = true
                val variableNode = variableNodeField.get(node)

                val valueField = node.javaClass.getDeclaredField("value")
                valueField.isAccessible = true
                val value = valueField.get(node)

                println("$indent│  ├─ Variable:")
                printASTTree(variableNode, "$indent│  │  ")

                println("$indent│  └─ Value:")
                printASTTree(value, "$indent   ")
            } catch (e: Exception) {
                println("$indent├─ DeclaratorNode (error: ${e.message})")
            }
        }
        "AssignmentNode" -> {
            try {
                val operatorField = node.javaClass.getDeclaredField("operator")
                operatorField.isAccessible = true
                val operator = operatorField.get(node)

                println("$indent├─ AssignmentNode (operator: $operator)")

                val leftField = node.javaClass.getDeclaredField("left")
                leftField.isAccessible = true
                val left = leftField.get(node)

                val rightField = node.javaClass.getDeclaredField("right")
                rightField.isAccessible = true
                val right = rightField.get(node)

                println("$indent│  ├─ Left:")
                printASTTree(left, "$indent│  │  ")

                println("$indent│  └─ Right:")
                printASTTree(right, "$indent   ")
            } catch (e: Exception) {
                println("$indent├─ AssignmentNode (error: ${e.message})")
            }
        }
        "BinaryOpNode" -> {
            try {
                val operatorField = node.javaClass.getDeclaredField("operator")
                operatorField.isAccessible = true
                val operator = operatorField.get(node)

                println("$indent├─ BinaryOpNode (operator: $operator)")

                val leftField = node.javaClass.getDeclaredField("left")
                leftField.isAccessible = true
                val left = leftField.get(node)

                val rightField = node.javaClass.getDeclaredField("right")
                rightField.isAccessible = true
                val right = rightField.get(node)

                println("$indent│  ├─ Left:")
                printASTTree(left, "$indent│  │  ")

                println("$indent│  └─ Right:")
                printASTTree(right, "$indent   ")
            } catch (e: Exception) {
                println("$indent├─ BinaryOpNode (error: ${e.message})")
            }
        }
        "LiteralNode" -> {
            try {
                val valueField = node.javaClass.getDeclaredField("value")
                valueField.isAccessible = true
                val value = valueField.get(node)

                val typeField = node.javaClass.getDeclaredField("type")
                typeField.isAccessible = true
                val type = typeField.get(node)

                println("$indent└─ LiteralNode: $value (type: $type)")
            } catch (e: Exception) {
                println("$indent└─ LiteralNode (error: ${e.message})")
            }
        }
        "VariableNode" -> {
            try {
                val nameField = node.javaClass.getDeclaredField("name")
                nameField.isAccessible = true
                val name = nameField.get(node)

                val typeField = node.javaClass.getDeclaredField("type")
                typeField.isAccessible = true
                val type = typeField.get(node)

                println("$indent└─ VariableNode: $name (type: $type)")
            } catch (e: Exception) {
                println("$indent└─ VariableNode (error: ${e.message})")
            }
        }
        "IdentifierNode" -> {
            try {
                val nameField = node.javaClass.getDeclaredField("name")
                nameField.isAccessible = true
                val name = nameField.get(node)

                println("$indent└─ IdentifierNode: $name")
            } catch (e: Exception) {
                println("$indent└─ IdentifierNode (error: ${e.message})")
            }
        }
        else -> {
            println("$indent└─ ${node.javaClass.simpleName} (unknown node type)")
        }
    }
}

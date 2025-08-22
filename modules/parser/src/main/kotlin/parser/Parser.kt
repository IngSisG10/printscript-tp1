package parser


import ast.abs.*
import ast.*
import exception.UnrecognizedLineException
import token.*
import token.abs.TokenInterface
import java.beans.Expression
import java.util.*

// Construccion de nodos
// Analisis Sintactico
// Analisis Semantico

class Parser(private val tokens: List<TokenInterface>) {

    private val listOfAST = mutableListOf<AstInterface>()
    private val semanticAnalyzer = SemanticAnalysis()

    fun parse(): List<AstInterface> {
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(this.tokens)

        // Syntactic Analysis
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line))
        }

        // Semantic Analysis
        semanticAnalyzer.analyze(listOfAST)

        return listOfAST
    }

    // Syntactic Analysis
    private fun parseLine(line: List<TokenInterface>) : AstInterface {
        val token = line[0] // let or a -> VariableDeclaratorToken or VariableToken
        return when (token) {

            is VariableDeclaratorToken -> {
                this.createDeclaratorAstNode(line)
            }

            is VariableToken  -> { // a = 5
                if (line[1] is OperationToken) { // =
                    this.createAssignmentAstNode(line)
                } else throw UnrecognizedLineException()
            }

            else -> throw UnrecognizedLineException()
        }
    }

    // Armado de nodos
    private fun createAssignmentAstNode(tokenList: List<TokenInterface>): AstInterface {
        return AssignmentNode(
            operator = Operation.EQUAL,
            left = IdentifierNode(tokenList[0].name),
            right = parseExpression(tokenList), // BinaryOpNode, LiteralNode, etc
        )
    }

    private fun createDeclaratorAstNode(tokenList: List<TokenInterface>): AstInterface {
        return DeclaratorNode(
            variableNode = VariableNode(
                name = tokenList[1].value.toString(), // a
                type = (tokenList[3] as TypeToken).value // Number
            ),
            //TODO: parse all possible operations
            value = VariableNode(name = "todo", type = Type.ANY), //this.parseOperation(line.subList(4, line.size)), // VariableNode
        )
    }

    private fun findVariableType(name: String): Type {
        for (ast in listOfAST) {
            if (ast is DeclaratorNode && ast.variableNode.name == name) {
                return ast.variableNode.type
            }
        }
        throw UnrecognizedLineException("Variable '$name' not found")
    }


    private fun splitTokensIntoLines(tokens: List<TokenInterface>): List<List<TokenInterface>> {
        val listOfTokensByLine = mutableListOf<MutableList<TokenInterface>>()
        var currentList = mutableListOf<TokenInterface>()

        for (token in tokens) {
            if (token.name == "end_sentence") {
                listOfTokensByLine.add(currentList)
                currentList = mutableListOf()
            } else {
                currentList.add(token)
            }
        }

        if (currentList.isNotEmpty()) {
            listOfTokensByLine.add(currentList)
        }
        return listOfTokensByLine
    }
}
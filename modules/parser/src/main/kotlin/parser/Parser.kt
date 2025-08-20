package parser

import ast.DeclaratorNode
import ast.VariableNode
import ast.abs.AstInterface
import exception.UnrecognizedLineException
import token.OperationToken
import token.Type
import token.TypeToken
import token.VariableDeclaratorToken
import token.VariableToken
import token.abs.TokenInterface

class Parser(
    private val tokens: List<TokenInterface>,
) {
    private val listOfAST = mutableListOf<AstInterface>()

    public fun parse(): List<AstInterface> {
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(this.tokens)
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line))
        }
        // TODO: add semantic analysis here
        return listOfAST
    }

    private fun parseLine(line: List<TokenInterface>): AstInterface {
        val token = line[0]
        return when (token) {
            is VariableDeclaratorToken -> {
                this.createDeclaratorAstNode(line)
            }

            is VariableToken -> {
                if (line[1] is OperationToken) {
                    this.createAsignatorAstNode(line)
                } else {
                    throw UnrecognizedLineException()
                }
            }

            else -> throw UnrecognizedLineException()
        }
    }

    private fun createAsignatorAstNode(line: List<TokenInterface>): Nothing {
        TODO("Not yet implemented")
    }

    private fun createDeclaratorAstNode(line: List<TokenInterface>): AstInterface =
        DeclaratorNode(
            variableNode =
                VariableNode(
                    name = (line[1] as VariableDeclaratorToken).name,
                    type = (line[3] as TypeToken).value,
                ),
            // TODO: parse all possible operations
            value = VariableNode(name = "todo", type = Type.ANY), // this.parseOperation(line.subList(4, line.size)),
            parent = null,
        )

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

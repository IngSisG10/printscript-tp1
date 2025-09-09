package parser

import common.ast.AstNode
import common.exception.UnrecognizedLineException
import common.token.NewLineToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import parser.nodecreator.AssignationNodeCreator
import parser.nodecreator.DeclaratorNodeCreator
import parser.nodecreator.FunctionNodeCreator
import parser.nodecreator.abs.AstNodeCreator

class Parser(
    private val tokens: List<TokenInterface>,
    private val nodeCreators: List<AstNodeCreator> =
        listOf(
            DeclaratorNodeCreator(),
            AssignationNodeCreator(),
            FunctionNodeCreator(),
        ),
) {
    private val listOfAST = mutableListOf<AstNode>()

    fun parse(): List<AstNode> {
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(this.tokens)
        addNodeToAst(listOfTokensByLine)
        // Semantic Analysis
        return listOfAST
    }

    private fun addNodeToAst(listOfTokensByLine: List<List<TokenInterface>>) {
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line))
        }
    }

    private fun parseLine(line: List<TokenInterface>): AstNode {
        val creator =
            nodeCreators.firstOrNull { it.matches(line) }
                ?: throw UnrecognizedLineException("No matching node creator for line starting with: ${line[0].name}")
        return creator.createAstNode(line)
    }

    private fun splitTokensIntoLines(tokens: List<TokenInterface>): List<List<TokenInterface>> {
        val listOfTokensByLine = mutableListOf<MutableList<TokenInterface>>()
        var currentList = mutableListOf<TokenInterface>()

        val filteredTokens = tokens.filterNot { it is WhiteSpaceToken || it is NewLineToken }

        for (token in filteredTokens) {
            if (token.name == "end_sentence") {
                if (currentList.isNotEmpty()) {
                    listOfTokensByLine.add(currentList)
                    currentList = mutableListOf()
                }
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

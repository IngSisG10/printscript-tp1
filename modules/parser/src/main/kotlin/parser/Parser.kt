package parser

import common.ast.AstNode
import common.exception.UnrecognizedLineException
import common.token.NewLineToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import parser.nodecreator.AssignationNodeCreator
import parser.nodecreator.DeclaratorNodeCreator
import parser.nodecreator.FunctionNodeCreator
import parser.nodecreator.IfStatementNodeCreator
import parser.nodecreator.SingleValueNodeCreator
import parser.nodecreator.abs.AstNodeCreator

class Parser(
    private val nodeCreators: List<AstNodeCreator> =
        listOf(
            // version 1.0
            DeclaratorNodeCreator(),
            AssignationNodeCreator(),
            FunctionNodeCreator(), // adapted for both versions 1.0 and 1.1
            // version 1.1
            IfStatementNodeCreator(),
            // edge cases
            SingleValueNodeCreator(),
        ),
) {
    fun parse(tokens: List<TokenInterface>): List<AstNode> {
        val listOfAST = mutableListOf<AstNode>()
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(tokens)
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line))
        }
        return listOfAST
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

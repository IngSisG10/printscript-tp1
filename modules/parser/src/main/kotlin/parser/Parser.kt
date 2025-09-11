package parser

import common.ast.AstNode
import common.exception.UnrecognizedLineException
import common.token.CloseBraceToken
import common.token.ElseToken
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.OpenBraceToken
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
        if (listOfTokensByLine.containsAll(listOf(emptyList()))) return emptyList()
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
        var depth = 0
        val listOfTokensByLine = mutableListOf<MutableList<TokenInterface>>()
        var currentList = mutableListOf<TokenInterface>()
        val filteredTokens = tokens.filterNot { it is WhiteSpaceToken || it is NewLineToken }

        if (filteredTokens.isEmpty()) return (listOf(filteredTokens))
        if (verifyEndIsCorrect(filteredTokens)) throw UnrecognizedLineException()

        for ((index, token) in filteredTokens.withIndex()) {
            if (token is OpenBraceToken) {
                depth++
            } else if (token is CloseBraceToken) {
                depth--
            }
            if (token.name == "end_sentence" && depth == 0) {
                if (currentList.isNotEmpty()) {
                    listOfTokensByLine.add(currentList)
                    currentList = mutableListOf()
                } else {
                    throw UnrecognizedLineException("empty declaration")
                }
            } else if (verifyParenthesisCase(token, depth, filteredTokens, index)) {
                if (currentList.isNotEmpty()) {
                    currentList.add(token)
                    listOfTokensByLine.add(currentList)
                    currentList = mutableListOf()
                } else {
                    throw UnrecognizedLineException("empty declaration")
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

    private fun verifyParenthesisCase(
        token: TokenInterface,
        depth: Int,
        tokens: List<TokenInterface>,
        index: Int,
    ) = (token is CloseBraceToken) &&
        (depth == 0) &&
        (((tokens.size - 1) == index) || ((tokens[index + 1] !is EndSentenceToken) && (tokens[index + 1] !is ElseToken)))

    private fun verifyEndIsCorrect(filteredTokens: List<TokenInterface>) =
        filteredTokens[filteredTokens.size - 1].name != "end_sentence" && filteredTokens[filteredTokens.size - 1].name != "close_brace"
}

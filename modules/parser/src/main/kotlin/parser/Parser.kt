package parser

import common.ast.AstNode
import common.exception.UnrecognizedLineException
import common.token.abs.TokenInterface
import parser.nodecreator.AssignationNodeCreator
import parser.nodecreator.DeclaratorNodeCreator
import parser.nodecreator.FunctionNodeCreator
import parser.nodecreator.IfStatementNodeCreator
import parser.nodecreator.SingleValueNodeCreator
import parser.nodecreator.UninitializedVariableNodeCreator
import parser.nodecreator.abs.AstNodeCreator
import parser.util.LineSplitter

class Parser(
    private val nodeCreators: List<AstNodeCreator> =
        listOf(
            DeclaratorNodeCreator(),
            AssignationNodeCreator(),
            FunctionNodeCreator(), // adapted for both versions 1.0 and 1.1
            IfStatementNodeCreator(), // adapted for both versions 1.0 and 1.1
            SingleValueNodeCreator(),
            UninitializedVariableNodeCreator(),
        ),
) {
    fun parse(tokens: List<TokenInterface>): List<AstNode> {
        val listOfAST = mutableListOf<AstNode>()
        // separate between ";"
        val listOfTokensByLine = LineSplitter.splitTokensIntoLines(tokens)
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
}

package parser.nodecreator

import common.ast.AstNode
import common.ast.FunctionNode
import common.token.FunctionToken
import common.token.ParenthesisToken
import common.token.abs.TokenInterface
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.FunctionValidator

class FunctionNodeCreator : AstNodeCreator {
    private val functionValidator = FunctionValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is FunctionToken

    override fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstNode>,
    ): AstNode {
        functionValidator.validate(line)
        val variableName = (line[0] as FunctionToken).value
        val parenthesisToken = line[1] as ParenthesisToken

        return FunctionNode(
            functionName = variableName,
            arguments = ExpressionParser.parseExpression(parenthesisToken.value, listOfAst), // Arguments parsing - ParenthesisToken.value
        )
    }
}

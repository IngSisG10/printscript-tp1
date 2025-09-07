package parser.nodecreator

import common.ast.FunctionNode
import common.token.FunctionToken
import common.token.ParenthesisToken
import common.token.abs.TokenInterface
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.FunctionValidator

class FunctionNodeCreator : AstNodeCreator {
    private val functionValidator = FunctionValidator()

    override fun matches(line: List<common.token.abs.TokenInterface>): Boolean = line.isNotEmpty() && line[0] is common.token.FunctionToken

    override fun createAstNode(
        line: List<common.token.abs.TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        functionValidator.validate(line)
        val variableName = (line[0] as common.token.FunctionToken).value
        val parenthesisToken = line[1] as common.token.ParenthesisToken

        return FunctionNode(
            functionName = variableName,
            arguments = ExpressionParser.parseExpression(parenthesisToken.value, listOfAst), // Arguments parsing - ParenthesisToken.value
        )
    }
}

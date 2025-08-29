package parser.nodecreator

import ast.FunctionNode
import ast.abs.AstInterface
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.FunctionValidator
import token.FunctionToken
import token.ParenthesisToken
import token.abs.TokenInterface

class FunctionNodeCreator : AstNodeCreator {
    private val functionValidator = FunctionValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is FunctionToken

    override fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        functionValidator.validate(line)
        val variableName = (line[0] as FunctionToken).value
        val parenthesisToken = line[1] as ParenthesisToken

        return FunctionNode(
            functionName = variableName,
            arguments = ExpressionParser.parseExpression(parenthesisToken.value, listOfAst), // Arguments parsing - ParenthesisToken.value
        )
    }
}

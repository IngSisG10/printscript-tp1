package parser.nodecreator

import ast.DeclaratorNode
import ast.VariableNode
import ast.abs.AstInterface
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.DeclarationValidator
import token.TypeToken
import token.VariableDeclaratorToken
import token.abs.TokenInterface

class DeclaratorNodeCreator : AstNodeCreator {
    private val declaratorValidator = DeclarationValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is VariableDeclaratorToken

    override fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        declaratorValidator.validate(line)
        val variableName = line[1].value.toString() // a
        val variableType = (line[3] as TypeToken).value // Number
        val valueTokensList = line.subList(5, line.size)

        return DeclaratorNode(
            variableNode =
                VariableNode(
                    name = variableName,
                    type = variableType,
                ),
            value = ExpressionParser.parseExpression(valueTokensList, listOfAst),
        )
    }
}

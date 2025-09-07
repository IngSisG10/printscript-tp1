package parser.nodecreator

import common.ast.AstNode
import common.ast.DeclaratorNode
import common.ast.VariableNode
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.DeclarationValidator

class DeclaratorNodeCreator : AstNodeCreator {
    private val declaratorValidator = DeclarationValidator()

    override fun matches(line: List<common.token.abs.TokenInterface>): Boolean =
        line.isNotEmpty() && line[0] is common.token.VariableDeclaratorToken

    override fun createAstNode(
        line: List<common.token.abs.TokenInterface>,
        listOfAst: List<AstNode>,
    ): AstNode {
        declaratorValidator.validate(line)
        val variableName = line[1].value.toString() // a
        val variableType = (line[3] as common.token.TypeToken).value // Number
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

package parser.nodecreator

import common.ast.AstNode
import common.ast.DeclaratorNode
import common.ast.VariableNode
import common.enums.DeclarationTypeEnum
import common.token.TypeToken
import common.token.VariableDeclaratorToken
import common.token.abs.TokenInterface
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.DeclarationValidator

class DeclaratorNodeCreator : AstNodeCreator {
    private val declaratorValidator = DeclarationValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is VariableDeclaratorToken

    override fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstNode>,
    ): AstNode {
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
            declarationType = DeclarationTypeEnum.LET,
        )
    }
}

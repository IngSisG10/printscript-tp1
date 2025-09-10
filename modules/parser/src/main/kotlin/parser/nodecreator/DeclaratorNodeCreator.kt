package parser.nodecreator

import common.ast.AstNode
import common.ast.DeclaratorNode
import common.ast.VariableNode
import common.enums.DeclarationTypeEnum
import common.token.TypeToken
import common.token.VariableDeclaratorToken
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.DeclarationValidator
import parser.nodecreator.validators.OperationsDeclValidator

class DeclaratorNodeCreator : AstNodeCreator {
    private val declaratorValidator = DeclarationValidator()
    private val operationCreator = OperationNodeCreator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is VariableDeclaratorToken

    override fun createAstNode(line: List<TokenInterface>): AstNode {
        declaratorValidator.validate(line)

        val variableName = line[1].value.toString()
        val variableType = (line[3] as TypeToken).value
        val operationTokensList = line.subList(5, line.size)

        val operationsDeclValidator = OperationsDeclValidator(variableType)
        operationsDeclValidator.validate(operationTokensList)

        return DeclaratorNode(
            variableNode =
                VariableNode(
                    name = variableName,
                    type = variableType,
                ),
            value = operationCreator.createAstNode(operationTokensList),
            declarationType = DeclarationTypeEnum.LET,
        )
    }
}

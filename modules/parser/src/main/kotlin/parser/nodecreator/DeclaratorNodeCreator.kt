package parser.nodecreator

import common.ast.AstNode
import common.ast.DeclaratorNode
import common.ast.VariableNode
import common.enums.DeclarationTypeEnum
import common.token.ConstantDeclaratorToken
import common.token.TypeToken
import common.token.VariableDeclaratorToken
import common.token.abs.TokenInterface
import parser.Parser
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.DeclarationValidator
import parser.nodecreator.validators.OperationsDeclValidator

class DeclaratorNodeCreator : AstNodeCreator {
    private val declaratorValidator = DeclarationValidator()
    private val operationCreator = OperationNodeCreator()

    override fun matches(line: List<TokenInterface>): Boolean =
        line.isNotEmpty() && (line[0] is VariableDeclaratorToken || line[0] is ConstantDeclaratorToken)

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
            value = operationCreator.createAstNode(operationTokensList), // Operation or Function (only one operation possible)
            declarationType = if (line[0] is VariableDeclaratorToken) DeclarationTypeEnum.LET else DeclarationTypeEnum.CONST,
        )
    }

    private fun parseOperation(operationTokensList: List<TokenInterface>): AstNode {
        val parserList = Parser().parse(operationTokensList)
        if (parserList.size != 1) {
            throw Exception("Only one operation/function is allowed in declaration")
        }
        return parserList[0]
    }
}

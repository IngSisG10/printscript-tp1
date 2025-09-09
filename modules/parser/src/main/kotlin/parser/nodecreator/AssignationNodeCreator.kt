package parser.nodecreator

import common.ast.AssignmentNode
import common.ast.AstNode
import common.ast.IdentifierNode
import common.enums.OperationEnum
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.AssignationValidator

class AssignationNodeCreator : AstNodeCreator {
    private val operationNodeCreator = OperationNodeCreator()
    private val assignationValidator = AssignationValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is VariableToken

    override fun createAstNode(line: List<TokenInterface>): AstNode {
        assignationValidator.validate(line)
        val assignationValue = line.subList(2, line.size)
        return AssignmentNode(
            operator = OperationEnum.EQUAL,
            left = IdentifierNode(line[0].value.toString()),
            right = operationNodeCreator.createAstNode(assignationValue),
        )
    }
}

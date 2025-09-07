package parser.nodecreator

import common.ast.AssignmentNode
import common.ast.AstNode
import common.ast.IdentifierNode
import common.enums.OperationEnum
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.AssignationValidator

class AssignationNodeCreator : AstNodeCreator {
    private val assignationValidator = AssignationValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is VariableToken

    override fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstNode>,
    ): AstNode {
        assignationValidator.validate(line)
        return AssignmentNode(
            operator = OperationEnum.EQUAL,
            left = IdentifierNode(line[0].value.toString()),
            right = ExpressionParser.parseExpression(line, listOfAst), // BinaryOpNode, LiteralNode, etc
        )
    }
}

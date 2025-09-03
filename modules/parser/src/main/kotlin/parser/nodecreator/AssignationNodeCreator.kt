package parser.nodecreator

import ast.AssignmentNode
import ast.IdentifierNode
import ast.abs.AstInterface
import enums.OperationEnum
import parser.AstNodeCreator
import parser.ExpressionParser
import parser.validators.AssignationValidator
import token.VariableToken
import token.abs.TokenInterface

class AssignationNodeCreator : AstNodeCreator {
    private val assignationValidator = AssignationValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is VariableToken

    override fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        assignationValidator.validate(line)
        return AssignmentNode(
            operator = OperationEnum.EQUAL,
            left = IdentifierNode(line[0].value.toString()),
            right = ExpressionParser.parseExpression(line, listOfAst), // BinaryOpNode, LiteralNode, etc
        )
    }
}

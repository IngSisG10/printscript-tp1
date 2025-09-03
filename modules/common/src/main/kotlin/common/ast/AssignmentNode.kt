package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor
import common.enums.OperationEnum

class AssignmentNode(
    val operator: common.enums.OperationEnum, // '='
    val left: IdentifierNode, // a
    val right: AstInterface, // BinaryOpNode, LiteralNode, etc.
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitAssignment(this)
    }
}

// AstInterface
// ExpressionNode (Interface)
// BinaryOpNode
// LiteralNode

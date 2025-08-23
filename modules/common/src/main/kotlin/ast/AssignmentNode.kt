package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.OperationEnum

class AssignmentNode(
    val operator: OperationEnum, // '='
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

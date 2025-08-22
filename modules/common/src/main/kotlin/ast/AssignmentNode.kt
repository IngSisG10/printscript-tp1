package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Operation

class AssignmentNode (
    val operator: Operation, // '='
    val left: IdentifierNode, // a
    val right: AstInterface, // BinaryOpNode, LiteralNode, etc.
): AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitAssignment(this)
    }
}

// AstInterface
// ExpressionNode (Interface)
// BinaryOpNode
// LiteralNode

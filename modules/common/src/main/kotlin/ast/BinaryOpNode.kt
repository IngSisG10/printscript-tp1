package ast

import ast.abs.*
import token.Operation

class BinaryOpNode(
    val operator: Operation,
    left: AstInterface, // todo: a Interface intermedia (composite pattern)
    right: AstInterface
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitBinaryOp(this)
    }
}
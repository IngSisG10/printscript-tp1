package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.OperationEnum

class BinaryOpNode(
    val operator: OperationEnum,
    val left: AstInterface, // todo: a Interface intermedia (composite pattern)
    val right: AstInterface
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitBinaryOp(this)
    }
}

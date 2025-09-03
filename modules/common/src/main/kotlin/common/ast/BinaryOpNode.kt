package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor
import common.enums.OperationEnum

class BinaryOpNode(
    val operator: common.enums.OperationEnum,
    val left: AstInterface, // todo: a Interface intermedia (composite pattern)
    val right: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitBinaryOp(this)
    }
}

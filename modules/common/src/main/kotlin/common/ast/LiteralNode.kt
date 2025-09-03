package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor
import common.enums.TypeEnum

class LiteralNode(
    val value: Any?,
    val type: common.enums.TypeEnum,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitLiteral(this)
    }
}

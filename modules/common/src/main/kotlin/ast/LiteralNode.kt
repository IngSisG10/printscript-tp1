package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.TypeEnum

class LiteralNode  (
    val value: Any?,
    val type: TypeEnum,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitLiteral(this)
    }
}

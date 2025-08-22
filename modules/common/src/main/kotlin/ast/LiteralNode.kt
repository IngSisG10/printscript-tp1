package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Type

class LiteralNode  (
    val value: Any?,
    val type: Type,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitLiteral(this)
    }
}

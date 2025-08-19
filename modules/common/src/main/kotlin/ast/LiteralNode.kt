package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Type

class LiteralNode  (
    override val parent: AstInterface? = null,
    val value: Any,
    val type: Type,
) : AstInterface {
    override val children = emptyList<AstInterface>()
    override fun accept(visitor: AstVisitor) {
        visitor.visitLiteral(this)
    }
}

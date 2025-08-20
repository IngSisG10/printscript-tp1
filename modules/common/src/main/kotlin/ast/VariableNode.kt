package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Type

class VariableNode(
    override val parent: AstInterface? = null,
    override val children: List<AstInterface> = emptyList(),
    val name: String,
    val type: Type = Type.ANY,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitVariable(this)
    }
}

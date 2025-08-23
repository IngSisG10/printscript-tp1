package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

class MonoOpNode(
    override val parent: AstInterface? = null,
    override val children: List<AstInterface> = emptyList(),
    val inner: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitMonoOp(this)
    }
}

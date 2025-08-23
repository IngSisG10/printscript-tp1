package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

data class IdentifierNode(
    val name: String,
    override val parent: AstInterface? = null,
) : AstInterface {
    override val children: List<AstInterface> = emptyList()

    override fun accept(visitor: AstVisitor) {
        visitor.visitIdentifier(this)
    }
}

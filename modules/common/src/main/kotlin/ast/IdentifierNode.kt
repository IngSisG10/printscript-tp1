package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

data class IdentifierNode(
    val name: String,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitIdentifier(this)
    }
}

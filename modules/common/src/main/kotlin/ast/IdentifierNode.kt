package ast

import ast.abs.*

data class IdentifierNode(
    val name: String
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitIdentifier(this)
    }
}
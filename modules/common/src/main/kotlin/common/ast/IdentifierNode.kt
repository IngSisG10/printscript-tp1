package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor

data class IdentifierNode(
    val name: String,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitIdentifier(this)
    }
}

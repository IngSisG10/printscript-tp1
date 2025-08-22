package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Type

class VariableNode(
    val name: String, // a
    val type: Type = Type.ANY // Number
): AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitVariable(this)
    }
}

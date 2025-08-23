package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.TypeEnum

class VariableNode(
    val name: String, // a
    val type: TypeEnum = TypeEnum.ANY // Number
): AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitVariable(this)
    }
}

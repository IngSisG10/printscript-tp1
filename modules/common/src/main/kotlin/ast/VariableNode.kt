package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.TypeEnum

class VariableNode(
    // a
    val name: String,
    // Number
    val type: TypeEnum = TypeEnum.ANY,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitVariable(this)
    }
}

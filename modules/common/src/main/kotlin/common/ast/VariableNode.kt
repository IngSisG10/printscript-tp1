package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor
import common.enums.TypeEnum

class VariableNode(
    // a
    val name: String,
    // Number
    val type: common.enums.TypeEnum = common.enums.TypeEnum.ANY,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitVariable(this)
    }
}

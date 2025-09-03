package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor
import common.enums.FunctionEnum

class FunctionNode(
    val functionName: common.enums.FunctionEnum, // println, sum, etc.
    val arguments: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitFunction(this)
    }
}

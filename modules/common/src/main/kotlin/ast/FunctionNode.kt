package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.FunctionEnum

class FunctionNode(
    val functionName: FunctionEnum, // println, sum, etc.
    val arguments: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitFunction(this)
    }
}

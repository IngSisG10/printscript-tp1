package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.FunctionEnum
// esta definido para el token

// todo: definirlo mejor
class FunctionNode(
    val functionName: FunctionEnum, // println, sum, etc.
    right: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitFunction(this)
    }
}

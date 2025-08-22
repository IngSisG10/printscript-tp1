package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Function // esta definido para el token

// todo: definirlo mejor
class FunctionNode (
    val functionName: Function, // println, sum, etc.
    right: AstInterface,

): AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitFunction(this)
    }
}
package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Function // esta definido para el token

class FunctionNode (
    val functionName: Function, // println, sum, etc.
    override val parent: AstInterface?,
    right: AstInterface,
    // override val children: List<AstInterface> // or (FunctionNode, VariableNode)
): AstInterface {

    // Children
    // println(x+5)
    // print("Result: " + c)
    private val _operation = arrayListOf<BinaryOpNode>()

    override val children: List<AstInterface> get() = _operation

    val right: AstInterface get() = _operation[0]

    override fun accept(visitor: AstVisitor) {
        visitor.visitFunction(this)
    }
}
package ast

import ast.abs.*
import token.Operation

class BinaryOpNode(
    override val parent: AstInterface?,
    val operator: Operation,
    left: AstInterface,
    right: AstInterface
) : AstInterface {

    private val _children = arrayListOf<AstInterface>()
    override val children: List<AstInterface> get() = _children

    init {
        _children += left
        _children += right
    }

    val left: AstInterface get() = _children[0]
    val right: AstInterface get() = _children[1]

    override fun accept(visitor: AstVisitor) {
        visitor.visitBinaryOp(this)
    }
}
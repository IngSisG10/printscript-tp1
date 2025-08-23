package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

class MonoOpNode(
    val inner: AstInterface

): AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitMonoOp(this)
    }
}

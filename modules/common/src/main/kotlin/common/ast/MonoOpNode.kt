package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor

class MonoOpNode(
    val inner: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitMonoOp(this)
    }
}

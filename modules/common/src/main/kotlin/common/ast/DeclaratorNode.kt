package common.ast

import common.ast.abs.AstInterface
import common.ast.abs.AstVisitor

class DeclaratorNode(
    val variableNode: VariableNode,
    val value: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitDeclarator(this)
    }
}

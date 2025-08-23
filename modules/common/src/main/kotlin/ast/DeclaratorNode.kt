package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

class DeclaratorNode(
    val variableNode: VariableNode,
    val value: AstInterface
): AstInterface {
    override fun accept(visitor: AstVisitor) {
        visitor.visitDeclarator(this)
    }
}

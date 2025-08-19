package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

class DeclaratorNode (
    val variableNode: VariableNode,
    val value: AstInterface,
    override val parent: AstInterface? = null,
    override val children: List<AstInterface> = emptyList()
): AstInterface {

    override fun accept(visitor: AstVisitor) {
        visitor.visitDeclarator(this)
    }

}
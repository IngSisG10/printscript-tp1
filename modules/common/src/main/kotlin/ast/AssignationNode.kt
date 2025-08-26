package ast

import ast.abs.AstInterface
import ast.abs.AstVisitor

class AssignationNode(
    val variableName: String,
    val value: AstInterface,
) : AstInterface {
    override fun accept(visitor: AstVisitor) {
        // visitor.visitAssignation(this)
    }
}

package ast.abs

import ast.BinaryOpNode
import ast.IdentifierNode

interface AstVisitor {
    fun visitBinaryOp(node: BinaryOpNode)
    fun visitIdentifier(node: IdentifierNode)

    // Optional generic fallback
    fun visitGeneric(node: AstInterface) { /* no-op */ }
}
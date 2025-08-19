package ast.abs

import ast.*

interface AstVisitor {
    fun visitBinaryOp(node: BinaryOpNode)
    fun visitIdentifier(node: IdentifierNode)
    fun visitLiteral(node: LiteralNode)
    fun visitDeclarator(node: DeclaratorNode)
    fun visitVariable(node: VariableNode)
    fun visitMonoOp(monoOpNode: MonoOpNode)

    // Optional generic fallback
    fun visitGeneric(node: AstInterface) { /* no-op */ }
}
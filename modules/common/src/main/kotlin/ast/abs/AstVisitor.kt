package ast.abs

import ast.BinaryOpNode
import ast.DeclaratorNode
import ast.IdentifierNode
import ast.LiteralNode
import ast.MonoOpNode
import ast.VariableNode

interface AstVisitor {
    fun visitBinaryOp(node: BinaryOpNode)

    fun visitIdentifier(node: IdentifierNode)

    fun visitLiteral(node: LiteralNode)

    fun visitDeclarator(node: DeclaratorNode)

    fun visitVariable(node: VariableNode)

    fun visitMonoOp(monoOpNode: MonoOpNode)

    fun visitFunction(node: FunctionNode)

    fun visitAssignment(node: AssignmentNode)

    // Optional generic fallback
    fun visitGeneric(node: AstInterface) { /* no-op */ }
}

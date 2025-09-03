package common.ast.abs

import common.ast.AssignmentNode
import common.ast.BinaryOpNode
import common.ast.DeclaratorNode
import common.ast.FunctionNode
import common.ast.IdentifierNode
import common.ast.LiteralNode
import common.ast.MonoOpNode
import common.ast.VariableNode

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

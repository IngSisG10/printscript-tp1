package common.ast

data class IfStatementNode(
    val condition: AstNode, // LiteralNode o VariableNode
    val thenBlock: BlockStatementNode,
    val elseBlock: BlockStatementNode?,
) : AstNode

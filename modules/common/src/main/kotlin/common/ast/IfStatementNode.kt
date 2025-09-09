package common.ast

data class IfStatementNode(
    val condition: AstNode,
    val thenBlock: BlockStatementNode,
    val elseBlock: BlockStatementNode?,
) : AstNode

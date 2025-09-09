package common.ast

data class BlockStatementNode(
    val statements: List<AstNode>,
) : AstNode

package common.ast

data class IfStatementNode(
    val condition: LiteralNode,
    val ifBody: AstNode,
    val elseBody: AstNode?,
) : AstNode

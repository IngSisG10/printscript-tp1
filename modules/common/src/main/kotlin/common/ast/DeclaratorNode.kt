package common.ast

data class DeclaratorNode(
    val variableNode: VariableNode,
    val value: AstNode,
    val isMutable: Boolean,
) : AstNode

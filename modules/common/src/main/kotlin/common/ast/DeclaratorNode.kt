package common.ast

import common.enums.DeclarationTypeEnum

data class DeclaratorNode(
    val variableNode: VariableNode,
    val value: AstNode,
    val declarationType: DeclarationTypeEnum,
) : AstNode

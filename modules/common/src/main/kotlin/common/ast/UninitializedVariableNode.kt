package common.ast

import common.enums.DeclarationTypeEnum

data class UninitializedVariableNode(
    val variableNode: VariableNode,
    val declarationType: DeclarationTypeEnum,
) : AstNode

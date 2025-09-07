package common.ast

import common.enums.TypeEnum

data class VariableNode(
    val name: String,
    val type: TypeEnum = TypeEnum.ANY,
) : AstNode

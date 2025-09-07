package common.ast

import common.enums.TypeEnum

data class LiteralNode(
    val value: Any?,
    val type: TypeEnum,
) : AstNode

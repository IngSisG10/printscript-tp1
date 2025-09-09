package common.ast

import common.enums.OperationEnum

data class MonoOpNode(
    val operator: OperationEnum,
    val inner: AstNode,
) : AstNode

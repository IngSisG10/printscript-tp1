package common.ast

import common.enums.OperationEnum

data class BinaryOpNode(
    val operator: OperationEnum,
    val left: AstNode,
    val right: AstNode,
) : AstNode

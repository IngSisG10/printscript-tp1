package common.ast

import common.enums.OperationEnum

data class AssignmentNode(
    val operator: OperationEnum,
    val left: IdentifierNode,
    val right: AstNode,
) : AstNode

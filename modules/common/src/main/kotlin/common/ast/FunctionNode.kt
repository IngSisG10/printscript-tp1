package common.ast

import common.enums.FunctionEnum

data class FunctionNode(
    val functionName: FunctionEnum,
    val arguments: AstNode,
) : AstNode

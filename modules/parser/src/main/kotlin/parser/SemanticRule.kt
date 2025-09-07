package parser

import common.ast.AstNode

interface SemanticRule {
    // fun canApply(node: AstInterface)
    fun analyze(node: AstNode): SemanticError?
}

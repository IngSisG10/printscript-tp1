package parser

import common.ast.abs.AstInterface

interface SemanticRule {
    // fun canApply(node: AstInterface)
    fun analyze(node: AstInterface): SemanticError?
}

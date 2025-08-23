package parser

import ast.abs.AstInterface

interface SemanticRule {
    // fun canApply(node: AstInterface)
    fun analyze(node: AstInterface): List<SemanticError>
}
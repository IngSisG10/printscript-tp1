package parser

interface SemanticRule {
    // fun canApply(node: AstInterface)
    fun analyze(node: AstInterface): SemanticError?
}

package common.ast.abs

interface AstInterface {
    // Visitor hook
    fun accept(visitor: AstVisitor)
}

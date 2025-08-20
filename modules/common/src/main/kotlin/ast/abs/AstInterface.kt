package ast.abs

interface AstInterface {
    val parent: AstInterface?
    val children: List<AstInterface>

    // Visitor hook
    fun accept(visitor: AstVisitor)

    // Depth-first traversal utility
    fun walk(action: (AstInterface) -> Unit) {
        action(this)
        children.forEach { it.walk(action) }
    }
}

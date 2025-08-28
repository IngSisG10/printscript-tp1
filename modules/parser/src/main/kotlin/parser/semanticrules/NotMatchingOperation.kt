package parser.semanticrules

import ast.BinaryOpNode
import ast.abs.AstInterface
import parser.SemanticError
import parser.SemanticRule

// BinaryOpNode
// todo: sacar esta logica de aca y ver bien que tipo de analisis semantico
// podemos tener en cuenta.
// Podes sumar un string + number
// Lo que hace es pasar el number a string y concatenar
class NotMatchingOperation : SemanticRule {
    override fun analyze(node: AstInterface): SemanticError? {
        if (node !is BinaryOpNode) return null

        val leftType = TypeAnalysis.getExpressionType(node.left) // Number
        val rightType = TypeAnalysis.getExpressionType(node.right) // String

        if (leftType != rightType) {
            return SemanticError("Arithmetic operation requires NUMBER types, got $leftType ${node.operator} $rightType")
        }

        return null
    }
}

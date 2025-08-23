package parser.semanticrules

import ast.BinaryOpNode
import ast.abs.AstInterface
import parser.SemanticError
import parser.SemanticRule
import token.Type

// BinaryOpNode
class NotMatchingOperation : SemanticRule {

    override fun analyze(node: AstInterface): List<SemanticError> {
        if (node !is BinaryOpNode) return emptyList()

        val errors = mutableListOf<SemanticError>()

        val leftType = TypeAnalysis.getExpressionType(node.left)
        val rightType = TypeAnalysis.getExpressionType(node.right)

        // todo: Type.Any == Type.Any

        if (leftType != rightType) {
            errors.add(
                SemanticError("Arithmetic operation requires NUMBER types, got $leftType ${node.operator} $rightType")
            )
        }

        return errors
    }
}
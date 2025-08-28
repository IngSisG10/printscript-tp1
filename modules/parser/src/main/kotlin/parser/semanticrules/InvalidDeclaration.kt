package parser.semanticrules

import ast.DeclaratorNode
import ast.abs.AstInterface
import enums.TypeEnum
import parser.SemanticError
import parser.SemanticRule

// let a: Number = "string"
// let b: String = 5 + 3
class InvalidDeclaration : SemanticRule {
    override fun analyze(node: AstInterface): SemanticError? {
        if (node is DeclaratorNode) {
            val expectedType = node.variableNode.type
            val actualType = TypeAnalysis.getExpressionType(node.value)
            if (expectedType != TypeEnum.ANY && expectedType != actualType) {
                return SemanticError(
                    "Type mismatch: expected $expectedType, but got $actualType"
                )
            }
        }
        return null
    }
}

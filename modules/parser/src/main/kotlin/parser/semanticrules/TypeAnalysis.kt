package parser.semanticrules

import ast.AssignmentNode
import ast.BinaryOpNode
import ast.DeclaratorNode
import ast.LiteralNode
import ast.abs.AstInterface
import enums.OperationEnum
import enums.TypeEnum

object TypeAnalysis {
    fun getExpressionType(node: AstInterface): TypeEnum =
        when (node) {
            is LiteralNode -> getLiteralType(node)
            is BinaryOpNode -> getBinaryOpType(node) // a = "5" + 5
            is AssignmentNode -> getAssignmentType(node)
            is DeclaratorNode -> getDeclaratorType(node)
            else -> TypeEnum.ANY // Exception -> UnrecognizedNodeError("Unknown node")
        }

    private fun getLiteralType(node: LiteralNode): TypeEnum =
        when (node.type) {
            TypeEnum.STRING -> TypeEnum.STRING
            TypeEnum.NUMBER -> TypeEnum.NUMBER
            else -> TypeEnum.ANY
        }

    private fun getDeclaratorType(node: DeclaratorNode): TypeEnum = getExpressionType(node.value)

    private fun getBinaryOpType(node: BinaryOpNode): TypeEnum {
        val leftType = getExpressionType(node.left)
        val rightType = getExpressionType(node.right)

        return when (node.operator) {
            OperationEnum.SUM -> {
                when {
                    leftType == TypeEnum.NUMBER && rightType == TypeEnum.NUMBER -> TypeEnum.NUMBER
                    leftType == TypeEnum.STRING && rightType == TypeEnum.STRING -> TypeEnum.STRING
                    else -> TypeEnum.ANY // O podrías lanzar error aquí
                }
            }

            OperationEnum.MINUS, OperationEnum.MULTIPLY, OperationEnum.DIVIDE -> {
                if (leftType == TypeEnum.NUMBER && rightType == TypeEnum.NUMBER) {
                    TypeEnum.NUMBER
                } else {
                    TypeEnum.ANY // O error para operaciones aritméticas inválidas
                }
            }

            else -> TypeEnum.ANY
        }
    }

    private fun getAssignmentType(node: AssignmentNode): TypeEnum = getExpressionType(node.right)
}

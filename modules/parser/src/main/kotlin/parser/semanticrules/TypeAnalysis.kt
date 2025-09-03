package parser.semanticrules

import common.ast.AssignmentNode
import common.ast.BinaryOpNode
import common.ast.DeclaratorNode
import common.ast.LiteralNode
import common.ast.VariableNode
import common.ast.abs.AstInterface
import common.enums.OperationEnum
import common.enums.TypeEnum

object TypeAnalysis {
    fun getExpressionType(node: AstInterface): common.enums.TypeEnum =
        when (node) {
            is LiteralNode -> getLiteralType(node)
            is BinaryOpNode -> getBinaryOpType(node) // a = "5" + 5
            is AssignmentNode -> getAssignmentType(node)
            is DeclaratorNode -> getDeclaratorType(node)
            is VariableNode -> getVariableType(node)
            else -> common.enums.TypeEnum.ANY // Exception -> UnrecognizedNodeError("Unknown node")
        }

    private fun getLiteralType(node: LiteralNode): common.enums.TypeEnum =
        when (node.type) {
            common.enums.TypeEnum.STRING -> common.enums.TypeEnum.STRING
            common.enums.TypeEnum.NUMBER -> common.enums.TypeEnum.NUMBER
            else -> common.enums.TypeEnum.ANY
        }

    private fun getDeclaratorType(node: DeclaratorNode): common.enums.TypeEnum = getExpressionType(node.value)

    private fun getBinaryOpType(node: BinaryOpNode): common.enums.TypeEnum {
        val leftType = getExpressionType(node.left)
        val rightType = getExpressionType(node.right)

        return when (node.operator) {
            common.enums.OperationEnum.SUM -> {
                when {
                    leftType == common.enums.TypeEnum.NUMBER && rightType == common.enums.TypeEnum.NUMBER -> common.enums.TypeEnum.NUMBER
                    leftType == common.enums.TypeEnum.STRING && rightType == common.enums.TypeEnum.STRING -> common.enums.TypeEnum.STRING
                    // TODO: manejar concatenación string + number y viceversa
                    else -> common.enums.TypeEnum.ANY // O podrías lanzar error aquí
                }
            }

            common.enums.OperationEnum.MINUS, common.enums.OperationEnum.MULTIPLY, common.enums.OperationEnum.DIVIDE -> {
                if (leftType == common.enums.TypeEnum.NUMBER && rightType == common.enums.TypeEnum.NUMBER) {
                    common.enums.TypeEnum.NUMBER
                } else {
                    common.enums.TypeEnum.ANY // O error para operaciones aritméticas inválidas
                }
            }

            else -> common.enums.TypeEnum.ANY
        }
    }

    private fun getVariableType(node: VariableNode): common.enums.TypeEnum = node.type

    private fun getAssignmentType(node: AssignmentNode): common.enums.TypeEnum = getExpressionType(node.right)
}

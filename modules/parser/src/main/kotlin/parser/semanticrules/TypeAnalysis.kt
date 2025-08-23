package parser.semanticrules

import ast.AssignmentNode
import ast.BinaryOpNode
import ast.FunctionNode
import ast.LiteralNode
import ast.abs.AstInterface
import token.Operation
import token.Type

object TypeAnalysis {
    fun getExpressionType(node: AstInterface): Type{
        return when(node){
            is LiteralNode -> getLiteralType(node)
            is BinaryOpNode -> getBinaryOpType(node)
            is AssignmentNode -> getAssignmentType(node)
            else -> Type.ANY
        }
    }

    private fun getLiteralType(node: LiteralNode): Type{
        return when(node.type){
            Type.STRING -> Type.STRING
            Type.NUMBER -> Type.NUMBER
            else -> Type.ANY
        }
    }

    private fun getBinaryOpType(node: BinaryOpNode): Type {
        val leftType = getExpressionType(node.left)
        val rightType = getExpressionType(node.right)

        return when(node.operator){
            Operation.SUM -> {
                when {
                    leftType == Type.NUMBER && rightType == Type.NUMBER -> Type.NUMBER
                    leftType == Type.STRING && rightType == Type.STRING -> Type.STRING
                    else -> Type.ANY // O podrías lanzar error aquí
                }
            }

            Operation.MINUS, Operation.MULTIPLY, Operation.DIVIDE -> {
                if (leftType == Type.NUMBER && rightType == Type.NUMBER) {
                    Type.NUMBER
                } else {
                    Type.ANY // O error para operaciones aritméticas inválidas
                }
            }

            else -> Type.ANY
        }
    }

    private fun getAssignmentType(node: AssignmentNode): Type{
        return getExpressionType(node.right)
    }
}
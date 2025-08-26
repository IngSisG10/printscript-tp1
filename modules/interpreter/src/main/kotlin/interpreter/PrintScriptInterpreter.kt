package interpreter

import ast.AssignmentNode
import ast.BinaryOpNode
import ast.DeclaratorNode
import ast.FunctionNode
import ast.IdentifierNode
import ast.LiteralNode
import ast.MonoOpNode
import ast.VariableNode
import ast.abs.AstInterface
import ast.abs.AstVisitor
import enums.OperationEnum
import exception.DivisionByZeroException
import exception.InterpreterException
import exception.TypeMismatchException

class PrintScriptInterpreter : AstVisitor {
    private val environment = Environment()
    private val output = mutableListOf<String>()
    private var currentValue: Value? = null

    fun interpret(astList: List<AstInterface>): List<String> {
        output.clear()
        for (astNode in astList) {
            astNode.accept(this)
        }
        return output.toList()
    }

    override fun visitBinaryOp(node: BinaryOpNode) {
        node.left.accept(this)
        val leftValue = currentValue ?: throw InterpreterException("Left operand did not produce a value")

        node.right.accept(this)
        val rightValue = currentValue ?: throw InterpreterException("Right operand did not produce a value")

        currentValue =
            when (node.operator) {
                OperationEnum.SUM -> evaluateAddition(leftValue, rightValue)
                OperationEnum.MINUS -> evaluateSubtraction(leftValue, rightValue)
                OperationEnum.MULTIPLY -> evaluateMultiplication(leftValue, rightValue)
                OperationEnum.DIVIDE -> evaluateDivision(leftValue, rightValue)
                else -> throw InterpreterException("Unknown operator: ${node.operator}")
            }
    }

    override fun visitIdentifier(node: IdentifierNode) {
        currentValue = environment.getValue(node.name)
    }

    private fun evaluateAddition(
        left: Value,
        right: Value,
    ): Value =
        when {
            left is NumberValue && right is NumberValue ->
                NumberValue(left.value + right.value)

            left is StringValue && right is StringValue ->
                StringValue(left.value + right.value)

            left is StringValue && right is NumberValue ->
                StringValue(left.value + right.toStringValue())

            left is NumberValue && right is StringValue ->
                StringValue(left.toStringValue() + right.value)

            else -> throw TypeMismatchException("Invalid operands for addition")
        }

    private fun evaluateSubtraction(
        left: Value,
        right: Value,
    ): Value {
        if (left is NumberValue && right is NumberValue) {
            return NumberValue(left.value - right.value)
        }
        throw TypeMismatchException("Subtraction requires two numbers")
    }

    private fun evaluateMultiplication(
        left: Value,
        right: Value,
    ): Value {
        if (left is NumberValue && right is NumberValue) {
            return NumberValue(left.value * right.value)
        }
        throw TypeMismatchException("Multiplication requires two numbers")
    }

    private fun evaluateDivision(
        left: Value,
        right: Value,
    ): Value {
        if (left is NumberValue && right is NumberValue) {
            if (right.value == 0.0) {
                throw DivisionByZeroException()
            }
            return NumberValue(left.value / right.value)
        }
        throw TypeMismatchException("Division requires two numbers")
    }

    override fun visitLiteral(node: LiteralNode) {}

    override fun visitDeclarator(node: DeclaratorNode) {}

    override fun visitVariable(node: VariableNode) {}

    override fun visitMonoOp(monoOpNode: MonoOpNode) {}

    override fun visitFunction(node: FunctionNode) {
        TODO("Not yet implemented")
    }

    override fun visitAssignment(node: AssignmentNode) {
        TODO("Not yet implemented")
    }
}

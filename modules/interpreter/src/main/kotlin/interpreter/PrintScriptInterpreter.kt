package interpreter

import exception.DivisionByZeroException
import exception.InterpreterException
import exception.TypeMismatchException

class PrintScriptInterpreter : AstVisitor {
    private val environment = Environment()
    private val output = mutableListOf<String>()
    private var currentValue: Value? = null

    fun interpret(ast: AstNode): List<String> {
        output.clear()
        ast.accept(this)
        return output.toList()
    }

    override fun visitBinaryOp(node: BinaryOpNode) {
        node.left.accept(this)
        val leftValue = currentValue ?: throw InterpreterException("Left operand did not produce a value")

        node.right.accept(this)
        val rightValue = currentValue ?: throw InterpreterException("Right operand did not produce a value")

        currentValue = when (node.operator) {
            "+" -> evaluateAddition(leftValue, rightValue)
            "-" -> evaluateSubtraction(leftValue, rightValue)
            "*" -> evaluateMultiplication(leftValue, rightValue)
            "/" -> evaluateDivision(leftValue, rightValue)
            else -> throw InterpreterException("Unknown operator: ${node.operator}")
        }
    }

    override fun visitIdentifier(node: IdentifierNode) {
        currentValue = environment.getValue(node.name)
    }

    private fun evaluateAddition(left: Value, right: Value): Value {
        return when {
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
    }

    private fun evaluateSubtraction(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            return NumberValue(left.value - right.value)
        }
        throw TypeMismatchException("Subtraction requires two numbers")
    }

    private fun evaluateMultiplication(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            return NumberValue(left.value * right.value)
        }
        throw TypeMismatchException("Multiplication requires two numbers")
    }

    private fun evaluateDivision(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            if (right.value == 0.0) {
                throw DivisionByZeroException()
            }
            return NumberValue(left.value / right.value)
        }
        throw TypeMismatchException("Division requires two numbers")
    }
}
package interpreter

import common.ast.AssignmentNode
import common.ast.AstNode
import common.ast.BinaryOpNode
import common.ast.DeclaratorNode
import common.ast.EmptyNode
import common.ast.FunctionNode
import common.ast.IdentifierNode
import common.ast.LiteralNode
import common.ast.MonoOpNode
import common.ast.VariableNode
import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.exception.DivisionByZeroException
import common.exception.InterpreterException
import common.exception.TypeMismatchException

class PrintScriptInterpreter {
    private val environment = Environment()
    private val output = mutableListOf<String>()

    fun interpret(astList: List<AstNode>): List<String> {
        output.clear()
        for (astNode in astList) {
            evaluate(astNode)
        }
        return output.toList()
    }

    private fun evaluate(node: AstNode): Value? =
        when (node) {
            is BinaryOpNode -> evaluateBinaryOp(node)
            is LiteralNode -> evaluateLiteral(node)
            is DeclaratorNode -> evaluateDeclarator(node)
            is VariableNode -> evaluateVariable(node)
            is IdentifierNode -> evaluateIdentifier(node)
            is AssignmentNode -> evaluateAssignment(node)
            is FunctionNode -> evaluateFunction(node)
            is MonoOpNode -> evaluateMonoOp(node)
            is EmptyNode -> evaluateEmpty()
        }

    private fun evaluateEmpty(): Value? = null

    private fun evaluateBinaryOp(node: BinaryOpNode): Value {
        val leftValue = evaluate(node.left) ?: throw InterpreterException("Left operand did not produce a value")
        val rightValue = evaluate(node.right) ?: throw InterpreterException("Right operand did not produce a value")

        return when (node.operator) {
            OperationEnum.SUM -> evaluateAddition(leftValue, rightValue)
            OperationEnum.MINUS -> evaluateSubtraction(leftValue, rightValue)
            OperationEnum.MULTIPLY -> evaluateMultiplication(leftValue, rightValue)
            OperationEnum.DIVIDE -> evaluateDivision(leftValue, rightValue)
            else -> throw InterpreterException("Unknown operator: ${node.operator}")
        }
    }

    private fun evaluateIdentifier(node: IdentifierNode): Value = environment.getValue(node.name)

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

    private fun evaluateLiteral(node: LiteralNode): Value {
        val literalValue = node.value
        return when (node.type) {
            TypeEnum.NUMBER -> {
                val value =
                    when (literalValue) {
                        is Number -> literalValue.toDouble()
                        is String ->
                            literalValue.toDoubleOrNull()
                                ?: throw InterpreterException("Invalid number literal: $literalValue")

                        else -> throw InterpreterException("Invalid number literal: $literalValue")
                    }
                NumberValue(value)
            }

            TypeEnum.STRING -> {
                StringValue(literalValue?.toString() ?: "")
            }

            TypeEnum.BOOLEAN -> {
                throw InterpreterException("Boolean type not supported in PrintScript 1.0")
            }

            TypeEnum.ANY -> {
                when (literalValue) {
                    is Number -> NumberValue(literalValue.toDouble())
                    is String -> StringValue(literalValue)
                    else -> throw InterpreterException("Unsupported literal type: $literalValue")
                }
            }
        }
    }

    private fun evaluateDeclarator(node: DeclaratorNode): Value? {
        val variable = node.variableNode
        val initialValue = evaluate(node.value)

        val typedValue =
            if (initialValue != null) {
                validateValueForType(initialValue, variable.type)
                initialValue
            } else {
                null
            }

        environment.declareVariable(variable.name, variable.type, typedValue)
        return null
    }

    private fun validateValueForType(
        value: Value,
        type: TypeEnum,
    ) {
        when (type) {
            TypeEnum.NUMBER -> {
                if (value !is NumberValue) {
                    throw TypeMismatchException("Cannot assign ${value::class.simpleName} to number variable")
                }
            }

            TypeEnum.STRING -> {
                if (value !is StringValue) {
                    throw TypeMismatchException("Cannot assign ${value::class.simpleName} to string variable")
                }
            }

            TypeEnum.ANY -> {}
            else -> throw InterpreterException("Unsupported type: $type")
        }
    }

    private fun evaluateVariable(node: VariableNode): Value = environment.getValue(node.name)

    private fun evaluateMonoOp(node: MonoOpNode): Value? = evaluate(node.inner)

    private fun evaluateFunction(node: FunctionNode): Value? {
        when (node.functionName) {
            FunctionEnum.PRINTLN -> {
                val value = evaluate(node.arguments) ?: throw InterpreterException("println argument did not produce a value")
                output.add(value.toStringValue())
            }
        }
        return null
    }

    private fun evaluateAssignment(node: AssignmentNode): Value? {
        if (node.operator != OperationEnum.EQUAL) {
            throw InterpreterException("Unsupported assignment operator: ${node.operator}")
        }
        val value = evaluate(node.right) ?: throw InterpreterException("Right operand did not produce a value")

        environment.setVariable(node.left.name, value)
        return null
    }
}

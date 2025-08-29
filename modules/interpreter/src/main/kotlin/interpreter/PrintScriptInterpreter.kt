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
import enums.FunctionEnum
import enums.OperationEnum
import enums.TypeEnum
import exception.DivisionByZeroException
import exception.InterpreterException
import exception.TypeMismatchException

// hacer un dispatcher para las funciones
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

    override fun visitLiteral(node: LiteralNode) {
        val literalValue = node.value
        currentValue =
            when (node.type) {
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

    override fun visitDeclarator(node: DeclaratorNode) {
        val variable = node.variableNode
        val initialValue =
            if (node.value != null) {
                node.value.accept(this)
                currentValue
            } else {
                null
            }

        val typedValue =
            if (initialValue != null) {
                validateValueForType(initialValue, variable.type)
                initialValue
            } else {
                null
            }

        environment.declareVariable(variable.name, variable.type, typedValue)
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

    override fun visitVariable(node: VariableNode) {
        return
    }

    override fun visitMonoOp(node: MonoOpNode) {
        node.inner.accept(this)
    }

    override fun visitFunction(node: FunctionNode) {
        when (node.functionName) {
            FunctionEnum.PRINTLN -> {
                node.arguments.accept(this)
                val value = currentValue ?: throw InterpreterException("println argument did not produce a value")
                output.add(value.toStringValue())
                currentValue = null
            }
        }
    }

    override fun visitAssignment(node: AssignmentNode) {
        if (node.operator != OperationEnum.EQUAL) {
            throw InterpreterException("Unsupported assignment operator: ${node.operator}")
        }

        node.right.accept(this)
        val value = currentValue ?: throw InterpreterException("Right operand did not produce a value")

        environment.setVariable(node.left.name, value)
        currentValue = null
    }
}

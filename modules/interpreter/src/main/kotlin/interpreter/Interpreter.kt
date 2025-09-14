package interpreter

import common.ast.AssignmentNode
import common.ast.AstNode
import common.ast.BinaryOpNode
import common.ast.BlockStatementNode
import common.ast.DeclaratorNode
import common.ast.FunctionNode
import common.ast.IdentifierNode
import common.ast.IfStatementNode
import common.ast.LiteralNode
import common.ast.MonoOpNode
import common.ast.UninitializedVariableNode
import common.ast.VariableNode
import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.exception.DivisionByZeroException
import common.exception.InterpreterException
import common.exception.InvalidTypeConversionError
import common.exception.TypeMismatchException

class Interpreter(
    private val inputProvider: (prompt: String) -> String = {
        print(it)
        readln()
    },
) {
    private val environment = Environment()
    private val output = mutableListOf<String>()

    fun interpret(astList: List<AstNode>): List<String> {
        for (astNode in astList) {
            evaluate(astNode)
        }
        val result = output.toList()
        output.clear()
        return result
    }

    private fun evaluate(node: AstNode): Value? =
        when (node) {
            is BinaryOpNode -> evaluateBinaryOp(node)
            is LiteralNode -> evaluateLiteral(node)
            is DeclaratorNode -> evaluateDeclarator(node)
            is UninitializedVariableNode -> evaluateUninitializedVariableDeclaration(node)
            is VariableNode -> evaluateVariable(node)
            is IdentifierNode -> evaluateIdentifier(node)
            is AssignmentNode -> evaluateAssignment(node)
            is FunctionNode -> evaluateFunction(node)
            is MonoOpNode -> evaluateMonoOp(node)
            is IfStatementNode -> evaluateIfStatement(node)
            is BlockStatementNode -> evaluateBlockStatement(node)
        }

    private fun evaluateUninitializedVariableDeclaration(node: UninitializedVariableNode): Value? {
        val variable = node.variableNode
        environment.declareVariable(variable.name, variable.type, null, node.declarationType)
        return null
    }

    private fun evaluateIfStatement(node: IfStatementNode): Value? {
        val condition = evaluate(node.condition)
        if (condition !is BooleanValue) {
            throw TypeMismatchException("If statement condition must be a boolean")
        }

        if (condition.value) {
            evaluate(node.thenBlock)
        } else {
            node.elseBlock?.let { evaluate(it) }
        }
        return null
    }

    private fun evaluateBlockStatement(node: BlockStatementNode): Value? {
        environment.enterScope()
        try {
            for (statement in node.statements) {
                evaluate(statement)
            }
        } finally {
            environment.exitScope()
        }
        return null
    }

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
                val value =
                    when (literalValue) {
                        is Boolean -> literalValue
                        is String ->
                            literalValue.toBooleanStrictOrNull()
                                ?: throw InvalidTypeConversionError("Cannot convert '$literalValue' to a boolean")
                        else -> throw InterpreterException("Invalid boolean literal: $literalValue")
                    }
                BooleanValue(value)
            }

            TypeEnum.ANY -> {
                when (literalValue) {
                    is Number -> NumberValue(literalValue.toDouble())
                    is String -> StringValue(literalValue)
                    is Boolean -> BooleanValue(literalValue)
                    else -> throw InterpreterException("Unsupported literal type: $literalValue")
                }
            }
        }
    }

    private fun evaluateDeclarator(node: DeclaratorNode): Value? {
        val variable = node.variableNode
        val initialValue =
            if (
                node.value is FunctionNode &&
                (
                    (node.value as FunctionNode).functionName == FunctionEnum.READ_INPUT ||
                        (node.value as FunctionNode).functionName == FunctionEnum.READ_ENV
                )
            ) {
                evaluateInputFunction(node.value as FunctionNode, variable.type)
            } else {
                evaluate(node.value)
            }

        val typedValue =
            if (initialValue != null) {
                validateValueForType(initialValue, variable.type)
                initialValue
            } else {
                null
            }

        environment.declareVariable(variable.name, variable.type, typedValue, node.declarationType)
        return null
    }

    private fun convertInputToType(
        input: String,
        type: TypeEnum,
    ): Value =
        when (type) {
            TypeEnum.STRING -> StringValue(input)
            TypeEnum.NUMBER ->
                NumberValue(
                    input.toDoubleOrNull() ?: throw InvalidTypeConversionError("Cannot convert '$input' to a number"),
                )
            TypeEnum.BOOLEAN ->
                BooleanValue(
                    input.toBooleanStrictOrNull() ?: throw InvalidTypeConversionError("Cannot convert '$input' to a boolean"),
                )
            else -> throw InterpreterException("Unsupported type for input conversion: $type")
        }

    private fun evaluateInputFunction(
        functionNode: FunctionNode,
        targetType: TypeEnum,
    ): Value {
        val argument =
            evaluate(functionNode.arguments)
                ?: throw InterpreterException("Argument for ${functionNode.functionName} did not produce a value")

        if (argument !is StringValue) {
            throw TypeMismatchException("${functionNode.functionName} argument must be a string")
        }

        val rawValue =
            when (functionNode.functionName) {
                FunctionEnum.READ_INPUT -> {
                    output.add(argument.value)
                    inputProvider(argument.value)
                }
                FunctionEnum.READ_ENV -> {
                    System.getenv(argument.value) ?: throw InterpreterException("Environment variable '${argument.value}' not found")
                }
                else -> throw InterpreterException("Not an input function") // Should not happen
            }

        return convertInputToType(rawValue, targetType)
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

            TypeEnum.BOOLEAN -> {
                if (value !is BooleanValue) {
                    throw TypeMismatchException("Cannot assign ${value::class.simpleName} to boolean variable")
                }
            }

            TypeEnum.ANY -> {}
        }
    }

    private fun evaluateVariable(node: VariableNode): Value = environment.getValue(node.name)

    private fun evaluateMonoOp(node: MonoOpNode): Value? = evaluate(node.inner)

    private fun evaluateFunction(node: FunctionNode): Value? {
        when (node.functionName) {
            FunctionEnum.PRINTLN -> {
                val value =
                    if (
                        node.arguments is FunctionNode &&
                        (
                            (node.arguments as FunctionNode).functionName == FunctionEnum.READ_INPUT ||
                                (node.arguments as FunctionNode).functionName == FunctionEnum.READ_ENV
                        )
                    ) {
                        evaluateInputFunction(node.arguments as FunctionNode, TypeEnum.STRING)
                    } else {
                        evaluate(node.arguments) ?: throw InterpreterException("println argument did not produce a value")
                    }
                output.add(value.toStringValue())
            }
            FunctionEnum.READ_INPUT, FunctionEnum.READ_ENV -> {
                throw InterpreterException("${node.functionName} can only be used as a variable initializer or a println argument")
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

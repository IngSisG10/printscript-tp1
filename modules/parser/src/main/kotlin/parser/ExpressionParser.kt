package parser

import ast.BinaryOpNode
import ast.DeclaratorNode
import ast.LiteralNode
import ast.VariableNode
import ast.abs.AstInterface
import enums.OperationEnum
import enums.TypeEnum
import exception.UnrecognizedLineException
import token.NumberLiteralToken
import token.OperationToken
import token.StringLiteralToken
import token.VariableToken
import token.abs.TokenInterface

object ExpressionParser {
    fun parseExpression(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        if (line.isEmpty()) {
            throw UnrecognizedLineException("Empty expression")
        }
        validateExpression(line)
        return parseAddition(line, listOfAst)
    }

    private fun validateExpression(line: List<TokenInterface>) {
        if (line.isEmpty()) {
            throw UnrecognizedLineException("Empty expression")
        }

        if (line[0] is OperationToken) { // Verificar. //Que el primer token no sea una operacion.
            throw UnrecognizedLineException("Expression cannot start with an operation: ${line[0].name}")
        }

        if (line.last() is OperationToken) { // Que el ultimo token no sea una operacion.
            throw UnrecognizedLineException("Expression cannot end with an operation: ${line.last().name}")
        }

        for (i in 1 until line.size) { // reviso que no haya dos operaciones seguidas.
            val token = line[i]
            if (token is OperationToken && line[i - 1] is OperationToken) {
                throw UnrecognizedLineException("Two consecutive operations found: ${line[i - 1].name} and ${line[i].name}")
            }
        }

        // line -> because work with a sublist [let, a, :, Number, = , 5, + , 5, * , 5]

        for (token in line) { // No puedo tener un igual en una expresion.
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                throw UnrecognizedLineException("Expression cannot contain an equal operation: ${token.name}")
            }
        }
    }

    private fun parseAddition(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        for (i in line.size - 1 downTo 0) {
            val token = line[i]
            if (token is OperationToken && (token.value == OperationEnum.SUM || token.value == OperationEnum.MINUS)) {
                val left = parseAddition(line.subList(0, i), listOfAst)
                val right = parseMultiplication(line.subList(i + 1, line.size), listOfAst)
                return BinaryOpNode(
                    operator = token.value as OperationEnum,
                    left = left,
                    right = right,
                )
            }
        }

        return parseMultiplication(line, listOfAst) // If no addition or subtraction found, parse multiplication
    }

    private fun parseMultiplication(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        if (line.isEmpty()) {
            throw UnrecognizedLineException("Empty multiplication expression")
        }

        for (i in line.size - 1 downTo 0) {
            val token = line[i]
            if (token is OperationToken && (token.value == OperationEnum.MULTIPLY || token.value == OperationEnum.DIVIDE)) {
                val left = parseMultiplication(line.subList(0, i), listOfAst)
                val right = parsePrimary(line.subList(i + 1, line.size), listOfAst)
                return BinaryOpNode(
                    operator = token.value as OperationEnum,
                    left = left,
                    right = right,
                )
            }
        }
        return parsePrimary(line, listOfAst)
    }

    private fun parsePrimary(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface {
        if (line.isEmpty()) {
            throw UnrecognizedLineException("Empty primary expression")
        }
        val token = line[0]
        if (token is VariableToken) {
            return VariableNode(
                name = token.value,
                type = findVariableType(token.value, listOfAst), // Assuming the variable type is known
            )
        } else if (token is NumberLiteralToken) {
            return LiteralNode(
                value = token.value,
                type = TypeEnum.NUMBER, // Assuming the type is number for literals
            )
        } else if (token is StringLiteralToken) {
            return LiteralNode(
                value = token.value,
                type = TypeEnum.STRING, // (maybe other option) -> findVariableType(token.value)
            )
        } else {
            throw UnrecognizedLineException("Unrecognized primary expression: ${token.name}")
        }
    }

    private fun findVariableType(
        name: String,
        listOfAst: List<AstInterface>,
    ): TypeEnum {
        for (ast in listOfAst) {
            if (ast is DeclaratorNode && ast.variableNode.name == name) {
                return ast.variableNode.type
            }
        }
        throw UnrecognizedLineException("Variable '$name' not found")
    }
}

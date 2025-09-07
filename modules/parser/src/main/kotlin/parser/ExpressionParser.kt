package parser

import common.ast.AstNode
import common.ast.BinaryOpNode
import common.ast.DeclaratorNode
import common.ast.LiteralNode
import common.ast.VariableNode
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.abs.TokenInterface

object ExpressionParser {
    fun parseExpression(
        line: List<TokenInterface>,
        listOfAst: List<AstNode>,
    ): AstNode {
        if (line.isEmpty()) {
            throw common.exception.UnrecognizedLineException("Empty expression")
        }
        validateExpression(line)
        return parseAddition(line, listOfAst)
    }

    private fun validateExpression(line: List<TokenInterface>) {
        if (line.isEmpty()) {
            throw common.exception.UnrecognizedLineException("Empty expression")
        }

        if (line[0] is common.token.OperationToken) { // Verificar. //Que el primer token no sea una operacion.
            throw common.exception.UnrecognizedLineException("Expression cannot start with an operation: ${line[0].name}")
        }

        if (line.last() is common.token.OperationToken) { // Que el ultimo token no sea una operacion.
            throw common.exception.UnrecognizedLineException("Expression cannot end with an operation: ${line.last().name}")
        }

        for (i in 1 until line.size) { // reviso que no haya dos operaciones seguidas.
            val token = line[i]
            if (token is common.token.OperationToken && line[i - 1] is common.token.OperationToken) {
                throw common.exception.UnrecognizedLineException(
                    "Two consecutive operations found: ${line[i - 1].name} and ${line[i].name}",
                )
            }
        }

        // line -> because work with a sublist [let, a, :, Number, = , 5, + , 5, * , 5]

        // fixme -> esta logica que onda?
//        for (token in line) { // No puedo tener un igual en una expresion.
//            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
//                throw UnrecognizedLineException("Expression cannot contain an equal operation: ${token.name}")
//            }
//        }
    }

    private fun parseAddition(
        line: List<TokenInterface>,
        listOfAst: List<AstNode>,
    ): AstNode {
        for (i in line.size - 1 downTo 0) {
            val token = line[i]
            if (token is common.token.OperationToken &&
                (token.value == OperationEnum.SUM || token.value == OperationEnum.MINUS)
            ) {
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
        listOfAst: List<AstNode>,
    ): AstNode {
        if (line.isEmpty()) {
            throw common.exception.UnrecognizedLineException("Empty multiplication expression")
        }

        for (i in line.size - 1 downTo 0) {
            val token = line[i]
            if (token is common.token.OperationToken &&
                (token.value == OperationEnum.MULTIPLY || token.value == OperationEnum.DIVIDE)
            ) {
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
        listOfAst: List<AstNode>,
    ): AstNode {
        if (line.isEmpty()) {
            throw common.exception.UnrecognizedLineException("Empty primary expression")
        }
        val token = line[0]
        if (token is common.token.VariableToken) {
            return VariableNode(
                name = token.value,
                type = findVariableType(token.value, listOfAst), // Assuming the variable type is known
            )
        } else if (token is common.token.NumberLiteralToken) {
            return LiteralNode(
                value = token.value,
                type = TypeEnum.NUMBER, // Assuming the type is number for literals
            )
        } else if (token is common.token.StringLiteralToken) {
            return LiteralNode(
                value = token.value,
                type = TypeEnum.STRING, // (maybe other option) -> findVariableType(token.value)
            )
        } else {
            throw common.exception.UnrecognizedLineException("Unrecognized primary expression: ${token.name}")
        }
    }

    private fun findVariableType(
        name: String,
        listOfAst: List<AstNode>,
    ): TypeEnum {
        for (ast in listOfAst) {
            if (ast is DeclaratorNode && ast.variableNode.name == name) {
                return ast.variableNode.type
            }
        }
        throw common.exception.UnrecognizedLineException("Variable '$name' not found")
    }
}

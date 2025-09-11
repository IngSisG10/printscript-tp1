package parser.nodecreator

import common.ast.AstNode
import common.ast.BinaryOpNode
import common.ast.MonoOpNode
import common.enums.OperationEnum
import common.token.CloseParenthesisToken
import common.token.NumberLiteralToken
import common.token.OpenParenthesisToken
import common.token.OperationToken
import common.token.VariableToken
import common.token.abs.OperationInterface
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.OperationValidator

class OperationNodeCreator : AstNodeCreator {
    private val singleValueNodeCreator = SingleValueNodeCreator()

    private val operationValidator = OperationValidator()

    override fun matches(line: List<TokenInterface>): Boolean {
        for (token in line) {
            if (token !is OperationInterface) {
                return false
            }
        }
        return true
    }

    // (5 + 5) * 1 / variable - 1
    override fun createAstNode(line: List<TokenInterface>): AstNode {
        operationValidator.validate(line)
        if (line.size == 1) {
            return singleValueNodeCreator.createAstNode(line)
        } else if (isMinusValueCase(line)) {
            return MonoOpNode(OperationEnum.MINUS, createAstNode(listOf(line[1])))
        } else if (hasSingleParenthesis(line)) {
            return createAstNode(line.subList(1, line.size - 1))
        }
        val opIndex = findHighestPriorityOp(line) ?: throw Exception("Unsupported operation")
        val opToken = line[opIndex].value as OperationEnum
        val left = createAstNode(line.subList(0, opIndex))
        val right = createAstNode(line.subList(opIndex + 1, line.size))
        return BinaryOpNode(opToken, left, right)
    }

    private fun isMinusValueCase(line: List<TokenInterface>): Boolean =
        line.size == 2 &&
            line[0] is OperationToken &&
            line[0].value == OperationEnum.MINUS &&
            (line[1] is NumberLiteralToken || line[1] is VariableToken)

    private fun findHighestPriorityOp(tokens: List<TokenInterface>): Int? {
        var highPriority = Int.MIN_VALUE
        var highIndex: Int? = null
        var depth = 0
        for ((i, token) in tokens.withIndex()) {
            if (token is OpenParenthesisToken) {
                depth++
            } else if (token is CloseParenthesisToken) {
                depth--
            } else if (token is OperationInterface && depth == 0) {
                val priority = giveTokenPriority(token, tokens, i)
                if (priority > highPriority) {
                    highPriority = priority
                    highIndex = i
                }
            }
        }
        return highIndex
    }

    private fun giveTokenPriority(
        token: OperationInterface,
        tokens: List<TokenInterface>,
        i: Int,
    ): Int =
        if (token is OperationToken && token.value == OperationEnum.MINUS) {
            if (tokens[i - 1] !is NumberLiteralToken && tokens[i - 1] !is VariableToken) {
                3
            } else {
                token.priority
            }
        } else {
            token.priority
        }

    private fun hasSingleParenthesis(line: List<TokenInterface>): Boolean {
        if (line[0] !is OpenParenthesisToken || line[line.size - 1] !is CloseParenthesisToken) return false
        var depth = 0
        var numberOfParenthesis = 0
        for (token in line) {
            if (token is OpenParenthesisToken) {
                if (depth == 0) numberOfParenthesis++
                depth++
            } else if (token is CloseParenthesisToken) {
                depth--
            }
        }
        return numberOfParenthesis == 1
    }
}

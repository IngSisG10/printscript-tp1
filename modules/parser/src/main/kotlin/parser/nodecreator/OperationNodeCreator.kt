package parser.nodecreator

import common.ast.AstNode
import common.ast.BinaryOpNode
import common.enums.OperationEnum
import common.token.CloseParenthesisToken
import common.token.OpenParenthesisToken
import common.token.abs.OperationInterface
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator

class OperationNodeCreator : AstNodeCreator {
    private val singleValueNodeCreator = SingleValueNodeCreator()

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
        if (line.size == 1) {
            return singleValueNodeCreator.createAstNode(line)
        } else if (hasSingleParenthesis(line)) {
            return createAstNode(line.subList(1, line.size - 1))
        }
        val opIndex = findHighestPriorityOp(line) ?: throw Exception("Unsupported operation")
        val opToken = line[opIndex].value as OperationEnum
        val left = createAstNode(line.subList(0, opIndex))
        val right = createAstNode(line.subList(opIndex + 1, line.size))
        return BinaryOpNode(opToken, left, right)
    }

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
                val priority = token.priority
                if (priority > highPriority) {
                    highPriority = priority
                    highIndex = i
                }
            }
        }
        return highIndex
    }

    private fun hasSingleParenthesis(line: List<TokenInterface>): Boolean {
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

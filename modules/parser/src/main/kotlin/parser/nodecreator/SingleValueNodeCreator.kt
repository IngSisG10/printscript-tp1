package parser.nodecreator

import common.ast.AstNode
import common.ast.IdentifierNode
import common.ast.LiteralNode
import common.enums.TypeEnum
import common.exception.UnrecognizedLineException
import common.token.BooleanLiteralToken
import common.token.NumberLiteralToken
import common.token.StringLiteralToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator

class SingleValueNodeCreator : AstNodeCreator {
    override fun matches(line: List<TokenInterface>): Boolean {
        TODO("Not yet implemented")
    }

    override fun createAstNode(line: List<TokenInterface>): AstNode {
        if (line.size > 1) throw Exception()
        return when (val token = line[0]) {
            is NumberLiteralToken -> {
                LiteralNode(
                    value = token.value,
                    type = TypeEnum.NUMBER,
                )
            }
            is StringLiteralToken -> {
                LiteralNode(
                    value = token.value,
                    type = TypeEnum.STRING,
                )
            }
            is VariableToken -> {
                IdentifierNode(
                    name = token.value,
                )
            }
            is BooleanLiteralToken -> {
                LiteralNode(
                    value = token.value,
                    type = TypeEnum.BOOLEAN,
                )
            }
            else -> throw UnrecognizedLineException("Unsupported value")
        }
    }
}

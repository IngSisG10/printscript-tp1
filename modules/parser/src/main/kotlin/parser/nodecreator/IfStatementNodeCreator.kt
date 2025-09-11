package parser.nodecreator

import common.ast.AstNode
import common.ast.BlockStatementNode
import common.ast.IdentifierNode
import common.ast.IfStatementNode
import common.ast.LiteralNode
import common.enums.TypeEnum
import common.exception.UnrecognizedLineException
import common.token.CloseBraceToken
import common.token.ElseToken
import common.token.IfToken
import common.token.OpenBraceToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.Parser
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.IfStatementValidator

class IfStatementNodeCreator : AstNodeCreator {
    private val ifStatementValidator = IfStatementValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is IfToken

    override fun createAstNode(line: List<TokenInterface>): AstNode {
        ifStatementValidator.validate(line)
        val booleanCondition = line[2].value.toString()

        // if VariableToken -> IdentifierNode
        // if BooleanLiteralToken -> LiteralNode

        // Find the closing bracket of the if block
        var ifBlockEnd = -1
        var openBrackets = 0
        for (i in line.indices) {
            if (line[i] is OpenBraceToken) openBrackets++
            if (line[i] is CloseBraceToken) {
                openBrackets--
                if (openBrackets == 0) {
                    ifBlockEnd = i
                    break
                }
            }
        }
        if (ifBlockEnd == -1) throw UnrecognizedLineException("Missing closing bracket for if block")

        val ifBodyTokens = line.subList(5, ifBlockEnd) // line[6]: after OpenBracketToken

        var elseBodyTokens: List<TokenInterface>? = null

        // Check if next token is ElseToken
        if (ifBlockEnd + 1 < line.size && line[ifBlockEnd + 1] is ElseToken) {
            // Find the opening and closing brackets for else block
            val elseOpenBracketIdx = ifBlockEnd + 2
            if (elseOpenBracketIdx >= line.size || line[elseOpenBracketIdx] !is OpenBraceToken) {
                throw UnrecognizedLineException("Missing opening bracket for else block or invalid token after else")
            }
            var elseBlockEnd = -1
            openBrackets = 1
            for (i in elseOpenBracketIdx + 1 until line.size) {
                if (line[i] is OpenBraceToken) openBrackets++
                if (line[i] is CloseBraceToken) {
                    openBrackets--
                    if (openBrackets == 0) {
                        elseBlockEnd = i
                        break
                    }
                }
            }
            if (elseBlockEnd == -1) throw UnrecognizedLineException("Missing closing bracket for else block")
            elseBodyTokens = line.subList(elseOpenBracketIdx + 1, elseBlockEnd)
        }

        return IfStatementNode(
            condition =
                if (line[2] is VariableToken) {
                    IdentifierNode(
                        name = line[2].value.toString(), // a
                    )
                } else {
                    // BooleanLiteralToken
                    LiteralNode(
                        value = booleanCondition, // true or false
                        type = TypeEnum.BOOLEAN, // BOOLEAN
                    )
                },
            thenBlock = BlockStatementNode(Parser().parse(ifBodyTokens)),
            elseBlock = if (elseBodyTokens != null) BlockStatementNode(Parser().parse(elseBodyTokens)) else null,
        )
    }
}

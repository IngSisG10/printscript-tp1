package parser.nodecreator

import common.ast.AstNode
import common.ast.IfStatementNode
import common.ast.LiteralNode
import common.enums.TypeEnum
import common.token.CloseBraceToken
import common.token.IfToken
import common.token.OpenBraceToken
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.IfStatementValidator

// if (){
//   ...
// }
// else - optional
class IfStatementNodeCreator : AstNodeCreator {
    private val operationCreator = OperationNodeCreator()
    private val ifStatementValidator = IfStatementValidator()
    /*
      ifToken
      OpenParenthesisToken
      BooleanLiteralToken
      CloseParenthesisToken
      OpenBracketToken
     */

    override fun matches(line: List<common.token.abs.TokenInterface>): Boolean = line.isNotEmpty() && line[0] is IfToken

    override fun createAstNode(line: List<common.token.abs.TokenInterface>): common.ast.AstNode {
        ifStatementValidator.validate(line)
        val booleanCondition = line[2].value.toString()

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
        if (ifBlockEnd == -1) throw Exception("Missing closing bracket for if block")

        val ifBodyTokens = line.subList(6, ifBlockEnd) // line[6]: after OpenBracketToken

        var elseBody: AstNode? = null
        // Check if next token is ElseToken
        if (ifBlockEnd + 1 < line.size && line[ifBlockEnd + 1] is common.token.ElseToken) {
            // Find the opening and closing brackets for else block
            val elseOpenBracketIdx = ifBlockEnd + 2
            if (elseOpenBracketIdx >= line.size || line[elseOpenBracketIdx] !is OpenBraceToken) {
                throw Exception("Missing opening bracket for else block")
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
            if (elseBlockEnd == -1) throw Exception("Missing closing bracket for else block")
            val elseBodyTokens = line.subList(elseOpenBracketIdx + 1, elseBlockEnd)
            elseBody = operationCreator.createAstNode(elseBodyTokens)
        }

        return IfStatementNode(
            condition =
                LiteralNode(
                    value = booleanCondition,
                    type = TypeEnum.BOOLEAN,
                ),
            ifBody = operationCreator.createAstNode(ifBodyTokens),
            // todo: puede recibir otro if aca dentro.
            //  How to handle (operationCreator) | BlockStatementCreator?
            elseBody = elseBody,
        )
    }
}

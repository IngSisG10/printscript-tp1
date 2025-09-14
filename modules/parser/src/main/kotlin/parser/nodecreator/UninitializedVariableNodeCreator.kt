package parser.nodecreator

import common.ast.AstNode
import common.ast.UninitializedVariableNode
import common.ast.VariableNode
import common.enums.DeclarationTypeEnum
import common.token.VariableDeclaratorToken
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.UninitializedVariableValidator

class UninitializedVariableNodeCreator : AstNodeCreator {
    private val uninitializedVariableValidator = UninitializedVariableValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.size < 5 && line[0] is VariableDeclaratorToken

    override fun createAstNode(line: List<TokenInterface>): AstNode {
        uninitializedVariableValidator.validate(line)

        val variableName = line[1].value.toString()
        val variableType = (line[3] as common.token.TypeToken).value

        return UninitializedVariableNode(
            variableNode =
                VariableNode(
                    name = variableName,
                    type = variableType,
                ),
            declarationType = if (line[0] is VariableDeclaratorToken) DeclarationTypeEnum.LET else DeclarationTypeEnum.CONST,
        )
    }
}

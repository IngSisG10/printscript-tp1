package parser.nodecreator

import common.ast.AstNode
import common.ast.FunctionNode
import common.token.FunctionToken
import common.token.abs.TokenInterface
import parser.nodecreator.abs.AstNodeCreator
import parser.nodecreator.validators.FunctionValidator

class FunctionNodeCreator : AstNodeCreator {
    private val operationNodeCreator = OperationNodeCreator()
    private val functionValidator = FunctionValidator()

    override fun matches(line: List<TokenInterface>): Boolean = line.isNotEmpty() && line[0] is FunctionToken

    override fun createAstNode(line: List<TokenInterface>): AstNode {
        functionValidator.validate(line)
        val variableName = (line[0] as FunctionToken).value
        val parenthesis = line.subList(1, line.size)

        return FunctionNode(
            functionName = variableName,
            arguments = operationNodeCreator.createAstNode(parenthesis),
        )
    }
}

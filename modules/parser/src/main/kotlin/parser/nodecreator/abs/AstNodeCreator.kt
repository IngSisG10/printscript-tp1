package parser.nodecreator.abs

import common.ast.AstNode
import common.token.abs.TokenInterface

interface AstNodeCreator {
    fun matches(line: List<TokenInterface>): Boolean

    fun createAstNode(line: List<TokenInterface>): AstNode
}

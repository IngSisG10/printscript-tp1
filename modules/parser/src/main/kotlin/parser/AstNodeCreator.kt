package parser

import common.token.abs.TokenInterface

// fixme: Es necesario que sepa el listado de ASTs?
interface AstNodeCreator {
    fun matches(line: List<common.token.abs.TokenInterface>): Boolean

    fun createAstNode(
        line: List<common.token.abs.TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface
}

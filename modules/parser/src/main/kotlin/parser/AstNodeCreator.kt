package parser

import ast.abs.AstInterface
import token.abs.TokenInterface

// fixme: Es necesario que sepa el listado de ASTs?
interface AstNodeCreator {
    fun matches(line: List<TokenInterface>): Boolean

    fun createAstNode(
        line: List<TokenInterface>,
        listOfAst: List<AstInterface>,
    ): AstInterface
}

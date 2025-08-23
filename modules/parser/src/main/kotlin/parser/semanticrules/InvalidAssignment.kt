package parser.semanticrules

import ast.abs.AstInterface
import parser.SemanticError
import parser.SemanticRule
import token.Type

class InvalidAssignment : SemanticRule {
    override fun analyze(node: AstInterface): List<SemanticError> {
        TODO()
    }
}
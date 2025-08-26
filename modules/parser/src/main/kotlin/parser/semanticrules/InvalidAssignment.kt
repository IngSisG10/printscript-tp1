package parser.semanticrules

import ast.abs.AstInterface
import parser.SemanticError
import parser.SemanticRule

class InvalidAssignment : SemanticRule {
    override fun analyze(node: AstInterface): SemanticError? {
        TODO()
    }
}

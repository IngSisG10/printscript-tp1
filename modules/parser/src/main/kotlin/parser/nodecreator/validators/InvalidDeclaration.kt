package parser.nodecreator.validators

class InvalidDeclaration
//
// import common.ast.AstNode
// import common.ast.DeclaratorNode
// import common.enums.TypeEnum
// import parser.semanticrules.SemanticError
// import parser.semanticrules.SemanticRule
// import parser.semanticrules.TypeAnalysis
//
// // TODO: hacerlo validator en vez de un semantic rule
//
// // let a: Number = "string"
// // let b: String = 5 + 3
// class InvalidDeclaration : SemanticRule {
//    override fun analyze(node: AstNode): SemanticError? {
//        if (node is DeclaratorNode) {
//            val expectedType = node.variableNode.type
//            val actualType = TypeAnalysis.getExpressionType(node.value)
//            if (expectedType != TypeEnum.ANY && expectedType != actualType) {
//                return SemanticError(
//                    "Type mismatch: expected $expectedType, but got $actualType",
//                )
//            }
//        }
//        return null
//    }
// }

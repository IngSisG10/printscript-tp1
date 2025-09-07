package interpreter

import common.ast.AstNode

interface Interpreter {
    fun interpret(astList: List<Any>): List<String>

    fun evaluate(node: AstNode): Value?

    fun evaluateBinaryOp(node: AstNode): Value?

    fun evaluateLiteral(node: AstNode): Value?

    fun evaluateDeclarator(node: AstNode): Value?

    fun evaluateVariable(node: AstNode): Value?

    fun evaluateIdentifier(node: AstNode): Value?

    fun evaluateAssignment(node: AstNode): Value?

    fun evaluateFunction(node: AstNode): Value?

    fun evaluateMonoOp(node: AstNode): Value?
}

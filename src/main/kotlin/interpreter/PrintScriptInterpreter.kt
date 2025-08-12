package interpreter

import parser.Node
import token.*
import token.Function
import java.util.*

class PrintScriptInterpreter : Interpreter {

    private val variables = mutableMapOf<String, Variable>()

    data class Variable(
        val value: Any?,
        val type: Type
    )

    override fun interpret(astList: List<Optional<Node>>) {
        for (ast in astList) {
            ast.ifPresent { node ->
                evaluate(node)
            }
        }
    }


    //todo: usar pattern matching
    private fun evaluate(node: Node): Any? {

        if (node.token is OperationToken &&
            node.token.value == Operation.EQUAL &&
            node.left.isPresent &&
            node.left.get().token is VariableDeclaratorToken
        ) {
            return evaluateVariableDeclaration(node)
        }

        return when (node.token) {
            is FunctionToken -> evaluateFunction(node)
            is OperationToken -> evaluateOperation(node)
            is TypeDeclaratorToken -> evaluateTypeDeclaration(node)

            else -> null
        }
    }

    private fun evaluateFunction(node: Node): Any? {
        val function = node as FunctionToken

        return when (function.value) {
            Function.PRINTLN -> {
                node.right.ifPresent { argNode ->
                    val value = evaluate(argNode)
                    println(value)
                }
                null
            }
        }
    }

    private fun evaluateVariableDeclaration(node: Node): Any? {

        var variableName: String? = null
        var variableType: Type? = null
        var value: Any? = null

        node.left.ifPresent { varDeclaratorNode ->
            if (varDeclaratorNode.token is VariableDeclaratorToken) {
                varDeclaratorNode.right.ifPresent { typeDeclaratorNode ->
                    if (typeDeclaratorNode.token is TypeDeclaratorToken) {
                        typeDeclaratorNode.left.ifPresent { varNode ->
                            if (varNode.token is VariableToken) {
                                variableName = varNode.token.value
                            }
                        }
                        typeDeclaratorNode.right.ifPresent { typeNode ->
                            if (typeNode.token is TypeToken) {
                                variableType = typeNode.token.value
                            }
                        }
                    }
                }
            }
        }

        node.right.ifPresent { valueNode ->
            value = evaluate(valueNode)
        }

        val varName = variableName
        val varType = variableType

        if (varName != null && varType != null && value != null) {
            variables[varName] = Variable(value, varType)
        }
        return null // TODO: revisar
    }

    private fun evaluateOperation(node: Node): Any? {
        val operation = node as OperationToken

        return when (operation.value) {
            Operation.EQUAL -> {
                node.left.ifPresent { leftNode ->
                    if (leftNode.token is VariableDeclaratorToken) {
                        val variableName = evaluate(leftNode)
                        if (variableName != null) {
                            node.right.ifPresent { rightNode ->
                                if (rightNode.token is OperationToken) {
                                    val realValue = evaluate(rightNode)
                                    if (realValue != null) {
                                        variables[variableName] = Variable(realValue, variables[variableName]!!.type)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun evaluateTypeDeclaration(node: Node): Any? {

        var variableName: String? = null
        var variableType: Type? = null
        var value: Any? = null

        node.left.ifPresent { leftNode ->
            if (leftNode.token is VariableToken) {
                variableName = leftNode.token.value
            }
        }
        node.right.ifPresent { rightNode ->
            if (rightNode.token is TypeToken) {
                variableType = rightNode.token.value
            }
        }

        val name = variableName
        val type = variableType

        if (name != null && type != null) {
            variables[name] = Variable(value, type)
        }

        return name
    }
    // TODO: revisar si no devolviese alguno de los 2
}
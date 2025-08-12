package org.example.parser

import org.example.token.Operation
import org.example.token.ParenthesisToken
import org.example.token.abs.TokenInterface
import java.util.*

class Parser(private val tokens: List<TokenInterface>) {

    private val listOfAST = mutableListOf<Optional<Node>>()

    public fun parse(): List<Optional<Node>> {
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(this.tokens);
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line));

        }
        return listOfAST
    }

    private fun parseLine(line: List<TokenInterface>) : Optional<Node> {
        if (line.size == 1) {
            if(line[0] is ParenthesisToken) {
                val parenthesis = line[0] as ParenthesisToken
                val parenthesisDataNode = this.parseLine(parenthesis.value)
                val node = Node(parenthesis, parenthesisDataNode, Optional.empty())
                return Optional.of(node)
            }
            else {
                return Optional.of(Node(line[0], Optional.empty(), Optional.empty()))
            }
        }
        if (line.isEmpty()) {
            return Optional.empty();
        }

        //loop
        val idx = findHighestPriorityIndex(line)
        val priorityToken = line[idx]
        val left = line.subList(0, idx)
        val right = line.subList(idx + 1, line.size)

        if(priorityToken is ParenthesisToken) {
            println("Parenthesis found")
            val parenthesisDataNode = this.parseLine(priorityToken.value)
            val node = Node(priorityToken, parenthesisDataNode, Optional.empty())
            return Optional.of(node)
        }
        else{
            val node = Node(priorityToken, parseLine(left), parseLine(right))
            return Optional.of(node)
        }
    }

    private fun findHighestPriorityIndex(line: List<TokenInterface>): Int {
        var bestIndex = 0
        var bestPrec = Int.MIN_VALUE

        for ((i, tok) in line.withIndex()) {
            val prec = getTokenPriority(tok)
            if (prec > bestPrec) {
                bestPrec = prec
                bestIndex = i
            }
        }

        return bestIndex
    }

    private fun getTokenPriority(token: TokenInterface): Int {
        return when (token.name) {
            "function" -> 3
            "operation" -> {
                when (token.value) {
                    Operation.EQUAL                         -> 6
                    Operation.SUM, Operation.MINUS          -> 4
                    Operation.MULTIPLY, Operation.DIVIDE    -> 3
                    else     -> 0 // operador desconocido, prioridad mínima
                }
            }
            "type_declarator" -> 4
            "variable_declarator" ->5
            "point" -> 1
            "parenthesis" -> -1

            else -> 0 // end_sentence (creo que no va pero bueno), variables, etc. no son operadores
        }
    }

    private fun splitTokensIntoLines(tokens: List<TokenInterface>): List<List<TokenInterface>> {
        val listOfTokensByLine = mutableListOf<MutableList<TokenInterface>>()
        var currentList = mutableListOf<TokenInterface>()

        for (token in tokens) {
            if (token.name == "end_sentence") {
                listOfTokensByLine.add(currentList)
                currentList = mutableListOf()
            } else {
                currentList.add(token)
            }
        }

        if (currentList.isNotEmpty()) {
            listOfTokensByLine.add(currentList)
        }
        return listOfTokensByLine
    }
}
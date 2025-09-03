package parser

import common.ast.abs.AstInterface
import common.exception.UnrecognizedLineException
import common.token.NewLineToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import parser.nodecreator.AssignationNodeCreator
import parser.nodecreator.DeclaratorNodeCreator
import parser.nodecreator.FunctionNodeCreator
import parser.semanticrules.InvalidDeclaration

// Construccion de nodos
// Analisis Sintactico
// Analisis Semantico

class Parser(
    private val tokens: List<common.token.abs.TokenInterface>,
    private val semanticRules: List<SemanticRule> =
        listOf(
            InvalidDeclaration(),
        ),
    private val nodeCreators: List<AstNodeCreator> =
        listOf(
            DeclaratorNodeCreator(),
            AssignationNodeCreator(),
            FunctionNodeCreator(),
        ),
) {
    private val listOfAST = mutableListOf<AstInterface>()

    private val semanticErrors = mutableListOf<SemanticError>()

    fun parse(): List<AstInterface> {
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(this.tokens)
        addNodeToAst(listOfTokensByLine)
        // Semantic Analysis
        checkSemanticRules()
        return listOfAST
    }

    private fun checkSemanticRules() {
        for (node in listOfAST) {
            for (rule in semanticRules) {
                val error = rule.analyze(node)
                if (error != null) {
                    semanticErrors.add(error)
                }
            }
        }

        if (semanticErrors.isNotEmpty()) {
            println("Semantic errors:")
            semanticErrors.forEach { println("  - ${it.message}") }
            throw RuntimeException("Semantic analysis failed")
        }
    }

    private fun addNodeToAst(listOfTokensByLine: List<List<common.token.abs.TokenInterface>>) {
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line))
        }
    }

    private fun parseLine(line: List<common.token.abs.TokenInterface>): AstInterface {
        val creator =
            nodeCreators.firstOrNull { it.matches(line) }
                ?: throw common.exception.UnrecognizedLineException("No matching node creator for line starting with: ${line[0].name}")
        return creator.createAstNode(line, listOfAST)
    }

    private fun splitTokensIntoLines(tokens: List<common.token.abs.TokenInterface>): List<List<common.token.abs.TokenInterface>> {
        val listOfTokensByLine = mutableListOf<MutableList<common.token.abs.TokenInterface>>()
        var currentList = mutableListOf<common.token.abs.TokenInterface>()

        val filteredTokens = tokens.filterNot { it is common.token.WhiteSpaceToken || it is common.token.NewLineToken }

        for (token in filteredTokens) {
            if (token.name == "end_sentence") {
                if (currentList.isNotEmpty()) {
                    listOfTokensByLine.add(currentList)
                    currentList = mutableListOf()
                }
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

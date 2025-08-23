package parser


import ast.abs.*
import ast.*
import exception.UnrecognizedLineException
import token.*
import token.abs.TokenInterface
import java.beans.Expression
import java.util.*

// Construccion de nodos
// Analisis Sintactico
// Analisis Semantico

class Parser(private val tokens: List<TokenInterface>) {

    private val listOfAST = mutableListOf<AstInterface>()

    fun parse(): List<AstInterface> {
        // separate between ";"
        val listOfTokensByLine = splitTokensIntoLines(this.tokens)

        // Syntactic Analysis
        for (line in listOfTokensByLine) {
            listOfAST.add(this.parseLine(line))
        }

        // todo: ver como llamar aca al analisis semantico
        // Semantic Analysis
        for (node in listOfAST){
            // todo; tener en cuenta la SemanticRules!
        }

        return listOfAST
    }

    private fun parseLine(line: List<TokenInterface>) : AstInterface {
        val token = line[0]
        return when (token) {

            is VariableDeclaratorToken -> { // line[0] == "let"
                this.createDeclaratorAstNode(line)
            }

            is VariableToken -> {
//                if (line[1] is OperationToken) { // line[0] == "variable and line[1] == "=" or other operation.
                    this.createAssignationAstNode(line)
//                } else throw UnrecognizedLineException()
            }

            else -> throw UnrecognizedLineException()
        }
    }

    private fun createAssignationAstNode(line: List<TokenInterface>): AstInterface {

        validateAssignationStructure(line)

        val variableName = (line[0] as VariableToken).value
        val variableType = findVariableType(variableName)

        return AssignmentNode(
            operator = Operation.EQUAL,
            left = IdentifierNode(line[0].name),
            right = parseExpression(line), // BinaryOpNode, LiteralNode, etc
        )
    }

    private fun createDeclaratorAstNode(line: List<TokenInterface>): AstInterface {

        validateDeclarationStructure(line)

//        name = tokenList[1].value.toString(), // a
//        type = (tokenList[3] as TypeToken).value // Number
        val variableName = (line[1] as VariableDeclaratorToken).value // fixme: No es un VariableDeclaratorToken
        val variableType = (line[3] as TypeToken).value
        val valueTokensList = line.subList(5, line.size)


        return DeclaratorNode(
            variableNode = VariableNode(
                name = variableName, // todo modificar y comprobar.
                type = variableType
            ),
            //TODO: parse all possible operations
            value = parseExpression(valueTokensList), //this.parseOperation(line.subList(4, line.size)),
            // valen: no me cierra, tengo que revisar por el momento.
        )
    }

    private fun validateAssignationStructure(line: List<TokenInterface>){

        if(line.size < 3){
            throw UnrecognizedLineException("Invalid assignment structure")
        }
        if(line[1] !is OperationToken && (line[1] as OperationToken).value != Operation.EQUAL){
            throw UnrecognizedLineException("Expected assignment operator, got: ${line[1].name}")
        }
    }


    private fun validateDeclarationStructure(line: List<TokenInterface>){

        if (line.size < 6 ){
            throw UnrecognizedLineException("Invalid declaration structure")
        }

        if(line[1] !is VariableToken){
            throw UnrecognizedLineException("Expected variable name, got: ${line[1].name}")
        } else if (line[2] !is TypeDeclaratorToken){
            throw UnrecognizedLineException("Expected type declarator, got: ${line[2].name}")
        } else if (line[3] !is TypeToken){
            throw UnrecognizedLineException("Expected type, got: ${line[3].name}")
        } else if (line[4] !is OperationToken || (line[4] as OperationToken).value != Operation.EQUAL){
            throw UnrecognizedLineException("Expected assignment operator '=', got: ${line[4].name}")
        }
    }

    private fun findVariableType(name: String): Type {
        for (ast in listOfAST) {
            if (ast is DeclaratorNode && ast.variableNode.name == name) {
                return ast.variableNode.type
            }
        }
        throw UnrecognizedLineException("Variable '$name' not found")
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

    private fun parseExpression(line: List<TokenInterface>): AstInterface { // Bien inicializado? Tiene que devolver algo ?
        if (line.isEmpty()) {
            throw UnrecognizedLineException("Empty expression")
        }
        validateExpression(line)
        return parseAddition(line) // TODO: implement other operations
    }

    private fun validateExpression(line: List<TokenInterface>) {
        if (line.isEmpty()) {
            throw UnrecognizedLineException("Empty expression")
        }

        if (line[0] is OperationToken){ // Verificar. //Que el primer token no sea una operacion.
            throw UnrecognizedLineException("Expression cannot start with an operation: ${line[0].name}")
        }

        if (tokens.last() is OperationToken){ // Que el ultimo token no sea una operacion.
            throw UnrecognizedLineException("Expression cannot end with an operation: ${tokens.last().name}")
        }

        for( i in line.size downTo 1) { // reviso que no haya dos operaciones seguidas.
            val token = line[i]
            if (token is OperationToken && line[i-1] is OperationToken){
                throw UnrecognizedLineException("Two consecutive operations found: ${line[i-1].name} and ${line[i].name}")
            }
        }

        for( token in tokens){ // No puedo tener un igual en una expresion.
            if (token is OperationToken && token.value == Operation.EQUAL){
                throw UnrecognizedLineException("Expression cannot contain an equal operation: ${token.name}")
            }
        }
    }




    private fun parseAddition(line: List<TokenInterface>): AstInterface {
        for (i in line.size - 1 downTo 0) {
            val token = line[i]
            if (token is OperationToken && (token.value == Operation.SUM || token.value == Operation.MINUS)){
                val left = parseAddition(line.subList(0, i))
                val right = parseMultiplication(line.subList(i + 1, line.size))
                return BinaryOpNode(
                    operator = token.value as Operation,
                    left = left,
                    right = right
                )
            }
        }

        return parseMultiplication(line) // If no addition or subtraction found, parse multiplication
    }

    private fun parseMultiplication(line: List<TokenInterface>): AstInterface {
        if (line.isEmpty()) { throw UnrecognizedLineException("Empty multiplication expression") }

        for (i in line.size - 1 downTo 0){
            val token = line[i]
            if ( token is OperationToken && (token.value == Operation.MULTIPLY || token.value == Operation.DIVIDE)){
                val left = parseMultiplication(line.subList(0,i))
                val right = parsePrimary(line.subList(i+1, line.size))
                return BinaryOpNode(
                    operator = token.value as Operation,
                    left = left,
                    right = right
                )
            }
        }
        return parsePrimary(line)
    }

    private fun parsePrimary(line: List<TokenInterface>): AstInterface {
        if (line.isEmpty()) { throw UnrecognizedLineException("Empty primary expression") }
        val token = line[0]
        if (token is VariableToken){
            return VariableNode(
                name = token.value,
                type = findVariableType(token.value), // Assuming the variable type is known
            )
        } else if (token is NumberLiteralToken) {
            return LiteralNode(
                value = token.value,
                type = Type.NUMBER, // Assuming the type is number for literals
            )
        } else if (token is StringLiteralToken) {
            return LiteralNode(
                value = token.value,
                type = findVariableType(token.value),)
        } else {
            throw UnrecognizedLineException("Unrecognized primary expression: ${token.name}")
        }
    }
}
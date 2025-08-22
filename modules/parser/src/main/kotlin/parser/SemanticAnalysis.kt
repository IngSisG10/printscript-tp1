package parser

import ast.*
import ast.abs.AstInterface
import ast.abs.AstVisitor
import token.Type

class SemanticAnalysis : AstVisitor{

    private val errors = mutableListOf<SemanticError>()

    fun analyze(ast: List<AstInterface>): List<SemanticError>{
        errors.clear()
        accept(ast)
        return errors.toList()
    }

    private fun accept(ast: List<AstInterface>) {
        for (node in ast) {
            node.accept(this);
        }
    }

    override fun visitLiteral(node: LiteralNode) {
        // Literals always are valid
    }


    // todo: Strategy para cada tipo de error posible

    // a = 5 + 5 * 5
    override fun visitAssignment(node: AssignmentNode) {
        // a
        // todo: static code analyzer
//        val leftSymbol = symbolTable.lookup(node.left.name)
//        if (leftSymbol == null){
//            errors.add(SemanticError("Variable '${node.left.name}' is not declared"))
//        }

        // Analyze (right node) -> AstInterface - BinaryOpNode, LiteralNode
        node.right.accept(this)

        // todo: getExpressionType logic
        // 5 + 5 * 5
        getExpressionType(node.right)
    }

    override fun visitBinaryOp(node: BinaryOpNode) {
        node.left.accept(this);
        node.right.accept(this);

        val leftType = getExpressionType(node.left)
        val rightType = getExpressionType(node.right)

        // todo: Cases (string + string | number + number)
        if (leftType != rightType){
            errors.add(SemanticError(
                "Arithmetic operation requires NUMBER types, got $leftType ${node.operator} $rightType"
            ))
        }
    }


    override fun visitFunction(node: FunctionNode) {
        TODO("Not yet implemented")
    }

    override fun visitDeclarator(node: DeclaratorNode) {
        TODO("Not yet implemented")
    }

    override fun visitVariable(node: VariableNode) {
        TODO("Not yet implemented")
    }

    override fun visitMonoOp(monoOpNode: MonoOpNode) {
        TODO("Not yet implemented")
    }

    private fun getExpressionType(expression: AstInterface): Type {
        TODO()
    }

    // todo: Probablemente, el hecho de analizar que la variable previamente haya sido declarada, es problema del STATIC CODE ANALYZER
    override fun visitIdentifier(node: IdentifierNode) {
        // todo: static code analyzer
//        val symbol = symbolTable.lookup(node.name)
//        if (symbol == null) {
//            errors.add(SemanticError("Variable '${node.name}' is not declared"))
//        }
    }
}
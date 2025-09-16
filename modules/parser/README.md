# Parser Module

## Overview
The Parser module is responsible for **syntactic analysis** - the second phase of the PrintScript compilation pipeline. It transforms the sequence of tokens produced by the lexer into an **Abstract Syntax Tree (AST)** that represents the hierarchical structure of the program.

## Architecture
The parser uses a **factory pattern** with specialized node creators, each responsible for recognizing and building specific types of AST nodes. This modular approach allows for easy extension and maintenance.

## Core Components

### 🎯 Main Classes

#### `Parser` (Primary Interface)
```kotlin
class Parser(private val nodeCreators: List<AstNodeCreator>)
```
- **Purpose**: Orchestrates the parsing process
- **Key Method**: `parse(tokens: List<TokenInterface>): List<AstNode>`
- **Process**:
  1. Splits tokens into logical lines (by semicolons)
  2. For each line, finds matching node creator
  3. Delegates AST node creation to appropriate creator
  4. Returns list of AST nodes representing the program

### 🏭 AST Node Creators
Located in `parser/nodecreator/`:

#### `DeclaratorNodeCreator`
- **Handles**: Variable declarations with initialization
- **Syntax**: `let x: Number = 42;` or `const name: String = "John";`
- **Creates**: `DeclaratorNode` containing variable info and initial value
- **Validation**: Ensures proper type annotations and initialization

#### `UninitializedVariableNodeCreator`
- **Handles**: Variable declarations without initialization
- **Syntax**: `let x: Number;`
- **Creates**: `UninitializedVariableNode` for deferred initialization
- **Note**: Only supported for `let` declarations, not `const`

#### `AssignationNodeCreator`
- **Handles**: Variable assignments
- **Syntax**: `x = 42;` or `name = "Alice";`
- **Creates**: `AssignmentNode` with left-hand variable and right-hand value
- **Validation**: Ensures variable exists and type compatibility

#### `FunctionNodeCreator`
- **Handles**: Function calls
- **Syntax**: `println("Hello");` or `readInput("Enter name: ");`
- **Creates**: `FunctionNode` with function name and arguments
- **Support**: Handles all built-in functions (println, readInput, readEnv)

#### `IfStatementNodeCreator`
- **Handles**: Conditional statements
- **Syntax**: `if (condition) { ... } else { ... }`
- **Creates**: `IfStatementNode` with condition, then-block, and optional else-block
- **Validation**: Ensures boolean conditions and proper block structure

#### `SingleValueNodeCreator`
- **Handles**: Simple expressions and literals
- **Syntax**: `42`, `"hello"`, `variableName`, `(expression)`
- **Creates**: Appropriate literal or identifier nodes
- **Fallback**: Catches simple cases not handled by other creators

### 🔍 Validation System
Located in `parser/nodecreator/validators/`:

#### Abstract Validators
- **`StructureValidator`**: Base class for syntax structure validation
- **`ParenthesisValidator`**: Ensures balanced parentheses and proper nesting

#### Specific Validators
- **`DeclarationValidator`**: Validates variable declaration syntax
- **`AssignationValidator`**: Validates assignment syntax and semantics
- **`FunctionValidator`**: Validates function call syntax and argument types
- **`OperationValidator`**: Validates arithmetic and logical operations
- **`IfStatementValidator`**: Validates conditional statement structure
- **`UninitializedVariableValidator`**: Validates uninitialized declarations
- **`OperationsDeclValidator`**: Validates complex expressions in declarations

### 🛠️ Utilities

#### `LineSplitter`
- **Purpose**: Splits token stream into logical statements
- **Method**: Uses semicolons (`;`) as statement delimiters
- **Handling**: Properly handles nested structures and string literals
- **Output**: List of token lists, each representing one statement

## Parsing Process

### 1. **Token Stream to Statements**
```
Tokens: [let, x, :, Number, =, 42, ;, println, (, x, ), ;]
       ↓ LineSplitter
Statements: [[let, x, :, Number, =, 42], [println, (, x, )]]
```

### 2. **Statement Classification**
```
For each statement, find matching creator:
[let, x, :, Number, =, 42] → DeclaratorNodeCreator
[println, (, x, )]         → FunctionNodeCreator
```

### 3. **AST Node Creation**
```
DeclaratorNode {
    variableNode: VariableNode("x", NUMBER),
    value: LiteralNode(42, NUMBER),
    declarationType: LET
}

FunctionNode {
    functionName: PRINTLN,
    arguments: IdentifierNode("x")
}
```

## Node Creator Priority

The parser tries node creators in this order:
1. **`DeclaratorNodeCreator`** - Variable declarations
2. **`AssignationNodeCreator`** - Assignments
3. **`FunctionNodeCreator`** - Function calls
4. **`IfStatementNodeCreator`** - Conditional statements
5. **`SingleValueNodeCreator`** - Simple expressions
6. **`UninitializedVariableNodeCreator`** - Uninitialized declarations

## Expression Handling

### Binary Operations
- **Operators**: `+`, `-`, `*`, `/`, `=`
- **Precedence**: Handled through recursive parsing
- **Types**: Arithmetic and assignment operations

### Nested Expressions
- **Parentheses**: Support for grouping `(a + b) * c`
- **Function Calls**: Nested calls `println(readInput("Name: "))`
- **Complex Assignments**: `x = (a + b) * c;`

## Version Support

### PrintScript 1.0
- **Variables**: `let` declarations
- **Types**: Number, String, Boolean, Any
- **Functions**: `println`
- **Operations**: Basic arithmetic

### PrintScript 1.1
- **Constants**: `const` declarations
- **Conditionals**: `if`/`else` statements
- **I/O**: `readInput`, `readEnv` functions
- **Booleans**: `true`, `false` literals

## Error Handling

### Syntax Errors
- **`UnrecognizedLineException`**: No matching node creator found
- **Position Info**: Includes token position for debugging

### Validation Errors
- **Type Mismatches**: Invalid type annotations
- **Structure Errors**: Malformed statements
- **Semantic Errors**: Undeclared variables, invalid operations

## AST Structure Examples

### Variable Declaration
```kotlin
// let x: Number = 42;
DeclaratorNode(
    variableNode = VariableNode("x", TypeEnum.NUMBER),
    value = LiteralNode(42, TypeEnum.NUMBER),
    declarationType = DeclarationTypeEnum.LET
)
```

### If Statement
```kotlin
// if (x > 0) { println("positive"); }
IfStatementNode(
    condition = BinaryOpNode(
        left = IdentifierNode("x"),
        operator = OperationEnum.GREATER,
        right = LiteralNode(0, TypeEnum.NUMBER)
    ),
    thenBlock = BlockStatementNode([
        FunctionNode(FunctionEnum.PRINTLN, LiteralNode("positive"))
    ]),
    elseBlock = null
)
```

## Usage Examples

### Basic Parsing
```kotlin
val parser = Parser()
val tokens = lexer.lex("let x: Number = 42;")
val ast = parser.parse(tokens)
```

### Error Handling
```kotlin
try {
    val ast = parser.parse(tokens)
} catch (e: UnrecognizedLineException) {
    println("Syntax error: ${e.message}")
}
```

## Dependencies
- **Common Module**: AST nodes, tokens, exceptions
- **No External Dependencies**: Pure Kotlin implementation

## Module Integration
```
Tokens → [Parser] → AST → [Interpreter/Formatter/Linter]
```

The parser serves as the bridge between lexical analysis and semantic processing, providing a structured representation that other modules can work with efficiently.

## Performance Characteristics
- **Time Complexity**: O(n) where n is the number of tokens
- **Space Complexity**: O(d) where d is the AST depth
- **Memory Usage**: Creates AST nodes incrementally
- **Error Recovery**: Fails fast with detailed error information
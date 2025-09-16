# Interpreter Module

## Overview
The Interpreter module is responsible for **executing PrintScript programs**. It takes the Abstract Syntax Tree (AST) produced by the parser and evaluates it, managing variables, executing functions, and producing output. This is the runtime engine of the PrintScript language.

## Architecture
The interpreter follows a **tree-walking evaluation pattern** with a **visitor-like approach**. It recursively evaluates AST nodes, maintains program state through an environment, and handles all runtime operations.

## Core Components

### 🎯 Main Classes

#### `Interpreter` (Primary Interface)
```kotlin
class Interpreter(private val inputProvider: (prompt: String) -> String)
```
- **Purpose**: Main execution engine for PrintScript programs
- **Key Method**: `interpret(astList: List<AstNode>): List<String>`
- **Features**:
  - Evaluates AST nodes recursively
  - Manages program output
  - Handles user input through configurable provider
  - Maintains execution environment

#### `Environment`
```kotlin
class Environment
```
- **Purpose**: Manages variable storage and scoping
- **Features**:
  - Variable declaration and assignment
  - Type checking and validation
  - Scope management (for block statements)
  - Constant vs mutable variable handling

### 💾 Value System
Located in `interpreter/`:

#### `Value` (Base Class)
- **Purpose**: Represents runtime values in the interpreter
- **Subclasses**: `NumberValue`, `StringValue`, `BooleanValue`
- **Methods**: Type conversion and string representation

#### Value Types
- **`NumberValue`**: Double-precision floating-point numbers
- **`StringValue`**: UTF-8 string values
- **`BooleanValue`**: Boolean true/false values

## Evaluation Engine

### 🌳 AST Node Evaluation

The interpreter evaluates different AST nodes through a comprehensive dispatch system:

#### Variable Operations
- **`DeclaratorNode`**: Creates new variables with initial values
- **`UninitializedVariableNode`**: Declares variables without initialization
- **`AssignmentNode`**: Updates existing variable values
- **`IdentifierNode`**: Retrieves variable values
- **`VariableNode`**: Accesses variable information

#### Expressions
- **`BinaryOpNode`**: Arithmetic operations (`+`, `-`, `*`, `/`)
- **`MonoOpNode`**: Unary operations and parenthesized expressions
- **`LiteralNode`**: Constant values (numbers, strings, booleans)

#### Control Flow
- **`IfStatementNode`**: Conditional execution with optional else blocks
- **`BlockStatementNode`**: Scoped code blocks

#### Functions
- **`FunctionNode`**: Built-in function calls (`println`, `readInput`, `readEnv`)

### 🔢 Arithmetic Operations

#### Addition (`+`)
```
Number + Number → Number    // 5 + 3 = 8
String + String → String    // "Hello" + "World" = "HelloWorld"
String + Number → String    // "Count: " + 42 = "Count: 42"
Number + String → String    // 42 + " items" = "42 items"
```

#### Other Operations (`-`, `*`, `/`)
- **Numbers Only**: Type-safe arithmetic
- **Division by Zero**: Throws `DivisionByZeroException`
- **Type Validation**: Ensures numeric operands

### 🏗️ Variable Management

#### Declaration Types
- **`let`**: Mutable variables that can be reassigned
- **`const`**: Immutable constants (assignment after declaration forbidden)

#### Type System
- **Static Typing**: Variables have declared types
- **Type Checking**: Runtime validation of assignments
- **Supported Types**: Number, String, Boolean, Any
- **Type Coercion**: Limited automatic conversion

#### Scoping
- **Block Scope**: Variables in `{}` blocks have limited lifetime
- **Scope Stack**: Nested scopes supported (if statements, etc.)
- **Variable Shadowing**: Inner scopes can shadow outer variables

### 🔧 Built-in Functions

#### `println(value)`
- **Purpose**: Output values to console
- **Type Handling**: Converts all values to strings
- **Output**: Adds to interpreter output list

#### `readInput(prompt)`
- **Purpose**: Read user input with prompt
- **Usage**: `let name: String = readInput("Enter name: ");`
- **Type Conversion**: Converts input to target variable type
- **Error Handling**: Throws `InvalidTypeConversionError` for invalid conversions

#### `readEnv(variableName)`
- **Purpose**: Read environment variables
- **Usage**: `let path: String = readEnv("PATH");`
- **Error Handling**: Throws `InterpreterException` if variable not found

## Runtime Features

### 🔍 Type Validation

#### Strong Typing
```
let x: Number = 42;        // ✓ Valid
let y: String = "hello";   // ✓ Valid
x = "invalid";             // ✗ TypeMismatchException
```

#### Type Conversion
```
let input: Number = readInput("Number: ");  // Converts string input to number
let display: String = readInput("Text: ");  // Keeps input as string
```

### 🎛️ Error Handling

#### Runtime Exceptions
- **`TypeMismatchException`**: Incompatible type operations
- **`DivisionByZeroException`**: Division by zero
- **`InterpreterException`**: General runtime errors
- **`InvalidTypeConversionError`**: Failed type conversions

#### Context Information
- Precise error messages with operation context
- Type information in error messages
- Variable name tracking for debugging

### 🔄 Control Flow

#### If Statements
```
if (condition) {
    // then block - executed if condition is true
} else {
    // else block - executed if condition is false
}
```

- **Condition Evaluation**: Must be boolean value
- **Block Scoping**: Variables declared in blocks are scoped
- **Nested Support**: If statements can be nested

## Usage Examples

### Basic Execution
```kotlin
val interpreter = Interpreter()
val output = interpreter.interpret(astNodes)
output.forEach { println(it) }
```

### Custom Input Provider
```kotlin
val interpreter = Interpreter { prompt ->
    println(prompt)
    readLine() ?: ""
}
```

### Error Handling
```kotlin
try {
    val output = interpreter.interpret(ast)
} catch (e: TypeMismatchException) {
    println("Type error: ${e.message}")
} catch (e: DivisionByZeroException) {
    println("Division by zero!")
}
```

## Program Execution Flow

### 1. **AST Processing**
```
AstNode List → Sequential Evaluation → Output Collection
```

### 2. **Variable Lifecycle**
```
Declaration → Type Validation → Storage → Usage → Scope Exit
```

### 3. **Expression Evaluation**
```
Complex Expression → Recursive Evaluation → Type Checking → Result
```

## Version Support

### PrintScript 1.0
- **Variables**: `let` declarations
- **Types**: Number, String, Boolean, Any
- **Functions**: `println`
- **Operations**: Arithmetic operations

### PrintScript 1.1
- **Constants**: `const` declarations
- **Conditionals**: `if`/`else` statements
- **I/O Functions**: `readInput`, `readEnv`
- **Boolean Literals**: `true`, `false` values

## Memory Management

### Environment Stack
- **Scope Management**: Automatic cleanup when exiting scopes
- **Variable Storage**: Efficient hash-based storage
- **Garbage Collection**: Relies on JVM garbage collection

### Output Management
- **Buffered Output**: Collects all output before returning
- **Memory Efficient**: Clears output buffer after each interpretation

## Dependencies
- **Common Module**: AST nodes, enums, exceptions
- **Kotlin Standard Library**: Core functionality
- **JVM Runtime**: For environment variable access

## Module Integration
```
AST → [Interpreter] → Output
                   ↓
              Runtime State
```

The interpreter serves as the execution engine, bringing PrintScript programs to life by evaluating the structured AST representation and managing all runtime concerns.

## Performance Characteristics
- **Time Complexity**: O(n) where n is the number of AST nodes
- **Space Complexity**: O(v + d) where v is variables and d is call depth
- **Memory Usage**: Proportional to variable count and nesting depth
- **Error Handling**: Immediate termination on runtime errors
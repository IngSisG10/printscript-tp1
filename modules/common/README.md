# Common Module

## Overview
The Common module serves as the foundational shared library for the PrintScript interpreter project. It contains core data structures, interfaces, and utilities that are used across all other modules in the system.

## Architecture
This module follows a clean architecture pattern with well-defined abstractions and clear separation of concerns. It provides the contract definitions that other modules implement.

## Key Components

### 📁 AST (Abstract Syntax Tree) Nodes
Located in `common/ast/`, these classes represent the parsed structure of PrintScript code:

- **`AstNode`** - Base interface for all AST nodes
- **`AssignmentNode`** - Represents variable assignments (e.g., `x = 5`)
- **`BinaryOpNode`** - Binary operations (e.g., `a + b`, `x * y`)
- **`MonoOpNode`** - Unary operations and parenthesized expressions
- **`BlockStatementNode`** - Code blocks enclosed in braces `{}`
- **`DeclaratorNode`** - Variable declarations with initialization
- **`UninitializedVariableNode`** - Variable declarations without initial values
- **`FunctionNode`** - Function calls (e.g., `println()`)
- **`IdentifierNode`** - Variable/identifier references
- **`IfStatementNode`** - Conditional statements with optional else blocks
- **`LiteralNode`** - Literal values (numbers, strings, booleans)
- **`VariableNode`** - Variable type and name information

### 🏷️ Token System
Located in `common/token/`, these classes represent lexical tokens:

#### Core Tokens
- **`TokenInterface`** - Base interface for all tokens
- **`OperationInterface`** - Interface for operation tokens
- **`VariableToken`** - Variable identifiers
- **`NumberLiteralToken`** - Numeric literals
- **`StringLiteralToken`** - String literals
- **`BooleanLiteralToken`** - Boolean literals (true/false)

#### Language Keywords
- **`VariableDeclaratorToken`** - `let` keyword
- **`ConstantDeclaratorToken`** - `const` keyword
- **`TypeDeclaratorToken`** - Type annotation colon `:`
- **`TypeToken`** - Type names (Number, String, Boolean, Any)
- **`IfToken`** - `if` keyword
- **`ElseToken`** - `else` keyword
- **`FunctionToken`** - Function names (println, readInput, readEnv)

#### Structural Tokens
- **`OpenParenthesisToken`** / **`CloseParenthesisToken`** - Parentheses `()`
- **`OpenBraceToken`** / **`CloseBraceToken`** - Braces `{}`
- **`OperationToken`** - Arithmetic operators (`+`, `-`, `*`, `/`, `=`)
- **`EndSentenceToken`** - Semicolon `;`
- **`PointToken`** - Decimal point `.`
- **`WhiteSpaceToken`** - Spaces and tabs
- **`NewLineToken`** - Line breaks

### 📊 Enumerations
Located in `common/enums/`:

- **`TypeEnum`** - Data types (NUMBER, STRING, BOOLEAN, ANY)
- **`OperationEnum`** - Operations (SUM, MINUS, MULTIPLY, DIVIDE, EQUAL)
- **`FunctionEnum`** - Built-in functions (PRINTLN, READ_INPUT, READ_ENV)
- **`DeclarationTypeEnum`** - Declaration types (LET, CONST)

### ⚠️ Exception System
Located in `common/exception/`, comprehensive error handling:

#### Core Exceptions
- **`InterpreterException`** - Runtime interpreter errors
- **`InvalidFileException`** - File access errors
- **`UnknownExpressionException`** - Unrecognized syntax
- **`UnrecognizedLineException`** - Unparseable lines

#### Syntax Validation Exceptions
- **`NoMatchingParenthesisException`** - Unbalanced parentheses
- **`NoMatchingQuotationMarksException`** - Unmatched quotes

#### Linting and Formatting Exceptions
- **`InvalidCamelCaseException`** - Identifier naming violations
- **`InvalidPascalCaseException`** - Type naming violations
- **`InvalidSnakeCaseException`** - Snake case violations
- **`NoSpaceBeforeColonException`** / **`NoSpaceAfterColonException`** - Spacing issues
- **`NoSpaceBeforeOperatorException`** / **`NoSpaceAfterOperatorException`** - Operator spacing
- **`NoSpaceBeforeAssignationException`** / **`NoSpaceAfterAssignationException`** - Assignment spacing
- **`InvalidNewLineBeforePrintlnException`** - Println formatting
- **`NoNewLineAfterSemiColon`** - Semicolon formatting
- **`TooManyBlankLinesException`** - Excessive whitespace
- **`MoreThanOneSpaceAfterTokenException`** - Multiple spaces
- **`InvalidIfBracePlacementException`** - If statement brace placement
- **`InvalidPrintLnArgumentException`** - Invalid println arguments

### 🔧 Utilities
- **`common/util/segmentsBySemicolon`** - Splits code by semicolons
- **`common/converter/Converter`** - Token-to-string conversion utilities

## Dependencies
- **External**: None (pure Kotlin standard library)
- **Internal**: Base module - no dependencies on other project modules

## Usage Pattern
```kotlin
// Typical usage in other modules
import common.ast.AstNode
import common.token.abs.TokenInterface
import common.enums.TypeEnum
import common.exception.InterpreterException
```

## Design Principles

### 1. **Interface Segregation**
Each component has well-defined interfaces that expose only necessary functionality.

### 2. **Immutability**
Most data structures are immutable, ensuring thread safety and preventing accidental modifications.

### 3. **Type Safety**
Strong typing with enums and sealed classes prevents runtime errors and improves code reliability.

### 4. **Extensibility**
The module is designed to support new language features without breaking existing functionality.

## Module Relationships
```
common (base) ←─── lexer
              ←─── parser
              ←─── interpreter
              ←─── formatter
              ←─── linter
              ←─── cli
```

The Common module serves as the foundation that all other modules build upon, providing consistent data structures and error handling throughout the entire PrintScript system.
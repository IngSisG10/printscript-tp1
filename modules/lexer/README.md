# Lexer Module

## Overview
The Lexer module is responsible for **lexical analysis** - the first phase of the PrintScript compilation pipeline. It transforms raw source code text into a sequence of meaningful tokens that can be processed by the parser.

## Architecture
The lexer follows a **rule-based pattern matching approach** where different token rules are applied in priority order to identify and extract tokens from the input stream.

## Core Components

### 🎯 Main Classes

#### `Lexer` (Primary Interface)
```kotlin
class Lexer(private val tokenRules: List<TokenRule>)
```
- **Purpose**: Main entry point for lexical analysis
- **Key Method**: `lex(segment: String): List<TokenInterface>`
- **Process**:
  1. Iterates character by character through input
  2. Applies token rules in priority order
  3. Handles whitespace and newlines specially
  4. Tracks row/column positions for error reporting

#### `TokenRule` (Abstract Base)
```kotlin
interface TokenRule {
    fun match(text: String, startIndex: Int, row: Int): TokenMatchResult?
}
```
- **Purpose**: Defines contract for token recognition rules
- **Returns**: `TokenMatchResult` with token, next index, and row delta

### 🔍 Token Recognition Rules
Located in `lexer/token/rules/`:

#### `StringLiteralRule`
- **Matches**: String literals enclosed in double quotes
- **Examples**: `"Hello World"`, `"123"`, `""`
- **Handles**: Escape sequences and multi-line strings
- **Error**: Throws `NoMatchingQuotationMarksException` for unclosed strings

#### `NumberLiteralRule`
- **Matches**: Numeric literals (integers and decimals)
- **Examples**: `42`, `3.14`, `0`, `123.456`
- **Format**: Supports floating-point notation
- **Validation**: Ensures proper decimal point usage

#### `KeywordRule` (Version 1.0)
- **Matches**: Language keywords for PrintScript 1.0
- **Keywords**: `let`, `println`, `Number`, `String`, `Boolean`, `Any`
- **Case-sensitive**: Exact string matching
- **Priority**: High (processed before identifiers)

#### `KeywordOnePointOneRule` (Version 1.1)
- **Extends**: KeywordRule for PrintScript 1.1
- **Additional Keywords**: `const`, `if`, `else`, `readInput`, `readEnv`, `true`, `false`
- **Backward Compatible**: Includes all 1.0 keywords

#### `ParenthesisRule`
- **Matches**: Parentheses and braces
- **Tokens**: `(`, `)`, `{`, `}`
- **Tracking**: Helps with balanced expression validation

#### `SingleCharRule` (Version 1.0)
- **Matches**: Single-character operators and punctuation
- **Characters**: `+`, `-`, `*`, `/`, `=`, `;`, `:`, `.`
- **Fast**: Direct character-to-token mapping

#### `SingleCharOnePointOneRule` (Version 1.1)
- **Extends**: SingleCharRule
- **No additional characters**: Same as 1.0 for now
- **Future-ready**: Prepared for language extensions

#### `IdentifierRule`
- **Matches**: Variable names and user-defined identifiers
- **Pattern**: Starts with letter/underscore, followed by alphanumeric/underscore
- **Examples**: `myVariable`, `_temp`, `counter123`
- **Priority**: Lowest (after keywords)

### 🛠️ Utilities

#### `RegexUtil`
Located in `lexer/token/rules/util/`:
- **Purpose**: Provides common regex patterns for token matching
- **Patterns**: Identifier validation, number formats, etc.
- **Reusability**: Shared across multiple token rules

#### `LexerUtil`
- **Factory Methods**: Creates version-specific lexer instances
- **Version Management**: Handles differences between PrintScript 1.0 and 1.1
- **Configuration**: Sets up appropriate token rules for each version

## Token Processing Pipeline

### 1. **Character-by-Character Processing**
```
Input: "let x: Number = 42;"
       ↓
Character stream: ['l','e','t',' ','x',':',...]
```

### 2. **Rule Application**
```
Priority Order:
1. StringLiteralRule    (highest)
2. NumberLiteralRule
3. KeywordRule
4. ParenthesisRule
5. SingleCharRule
6. IdentifierRule       (lowest)
```

### 3. **Token Generation**
```
Output: [VariableDeclaratorToken, WhiteSpaceToken, VariableToken,
         TypeDeclaratorToken, TypeToken, ...]
```

## Version Support

### PrintScript 1.0
- **Basic Types**: Number, String, Boolean, Any
- **Declarations**: `let` variables
- **Functions**: `println`
- **Operations**: Arithmetic and assignment

### PrintScript 1.1
- **Extends 1.0** with:
- **Constants**: `const` declarations
- **Conditionals**: `if`/`else` statements
- **I/O Functions**: `readInput`, `readEnv`
- **Boolean Literals**: `true`, `false`

## Error Handling

### Position Tracking
- **Row/Column**: Maintains precise source location for each token
- **Error Context**: Provides meaningful error messages with location info

### Exception Types
- **`UnknownExpressionException`**: Unrecognized character sequences
- **`NoMatchingQuotationMarksException`**: Unclosed string literals

## Usage Examples

### Basic Lexing
```kotlin
val lexer = Lexer()
val tokens = lexer.lex("let x: Number = 42;")
// Returns: [VariableDeclaratorToken, WhiteSpaceToken, VariableToken("x"), ...]
```

### Version-Specific Lexing
```kotlin
val lexer10 = LexerUtil.createLexer("1.0")
val lexer11 = LexerUtil.createLexer("1.1")
```

## Dependencies
- **Common Module**: Uses token interfaces and exception types
- **Kotlin Standard Library**: Regex and string processing

## Module Integration
```
Source Code → [Lexer] → Tokens → [Parser] → AST
```

The lexer serves as the critical first step, converting unstructured text into structured tokens that the parser can understand and process into an Abstract Syntax Tree.

## Performance Characteristics
- **Time Complexity**: O(n) where n is the input length
- **Space Complexity**: O(t) where t is the number of tokens
- **Memory Efficient**: Processes input incrementally without loading entire file
- **Error Fast**: Fails quickly on invalid syntax with precise error locations
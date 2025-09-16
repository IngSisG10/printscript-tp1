# Formatter Module

## Overview
The Formatter module is responsible for **automatic code formatting** in PrintScript. It takes tokenized code and applies a series of formatting rules to ensure consistent code style, proper spacing, indentation, and line breaks according to configurable standards.

## Architecture
The formatter uses a **chain of responsibility pattern** with individual formatting fixes that are applied sequentially. Each fix transforms the token stream to enforce specific formatting rules, and the final result is converted back to formatted text.

## Core Components

### 🎯 Main Classes

#### `Formatter` (Primary Interface)
```kotlin
class Formatter(private val formatterFixes: List<FormatterFix>)
```
- **Purpose**: Orchestrates the formatting process
- **Key Method**: `format(tokens: List<TokenInterface>): String`
- **Process**:
  1. Applies each formatting fix in sequence
  2. Each fix transforms the token list
  3. Converts final tokens back to formatted string
  4. Returns properly formatted code

#### `Converter`
- **Purpose**: Converts token lists back to text representation
- **Location**: `common/converter/Converter`
- **Function**: Token-to-string serialization

### 🛠️ Formatting Fix System
Located in `formatter/fixes/`:

#### Abstract Base Classes

##### `FormatterFix`
```kotlin
abstract class FormatterFix {
    abstract fun fix(tokens: List<TokenInterface>): List<TokenInterface>
}
```
- **Purpose**: Base class for all formatting transformations
- **Pattern**: Takes token list, returns modified token list
- **Chaining**: Supports sequential application of multiple fixes

##### `FixSettings`
- **Purpose**: Configuration base for formatting rules
- **Serialization**: JSON-based configuration support
- **Customization**: Allows rule-specific settings

### 📏 Required Formatting Fixes
Located in `formatter/fixes/required/`:

#### `SpaceBeforeColonFix`
- **Rule**: Ensures space before type annotations
- **Example**: `x:Number` → `x : Number`
- **Exception**: Throws `NoSpaceBeforeColonException` if violated

#### `SpaceAfterColonFix`
- **Rule**: Ensures space after type annotations
- **Example**: `x:Number` → `x: Number`
- **Exception**: Throws `NoSpaceAfterColonException` if violated

#### `SpaceBeforeAndAfterEqualFix`
- **Rule**: Ensures spaces around assignment operators
- **Example**: `x=42` → `x = 42`
- **Coverage**: Handles all assignment operations

#### `SpaceBeforeAndAfterOperatorFix`
- **Rule**: Ensures spaces around arithmetic operators
- **Example**: `a+b*c` → `a + b * c`
- **Operators**: `+`, `-`, `*`, `/`

#### `OneSpaceAfterTokenMaxFix`
- **Rule**: Limits multiple spaces to single space
- **Example**: `let    x` → `let x`
- **Exception**: Throws `MoreThanOneSpaceAfterTokenException`

#### `LineJumpAfterSemiColonFix`
- **Rule**: Ensures newline after semicolons
- **Example**: `x = 42; y = 24;` → `x = 42;\ny = 24;`
- **Exception**: Throws `NoNewLineAfterSemiColon`

#### `LineJumpSpaceBeforePrintlnFix`
- **Rule**: Ensures proper spacing before println calls
- **Example**: Fixes newline placement around println statements
- **Exception**: Throws `InvalidNewLineBeforePrintlnException`

#### `IfBraceSameLinePlacementFix`
- **Rule**: Enforces brace placement for if statements
- **Style**: Opening brace on same line as if statement
- **Example**:
  ```
  if (condition)
  {  →  if (condition) {
  ```
- **Exception**: Throws `InvalidIfBracePlacementException`

### 🎨 Custom Formatting Fixes
Located in `formatter/fixes/custom/`:

#### `MaxOneBlankLineFix`
- **Rule**: Limits consecutive blank lines
- **Maximum**: One blank line between code sections
- **Example**: Multiple empty lines → Single empty line
- **Exception**: Throws `TooManyBlankLinesException`

## Configuration System

### 📋 Serializable Settings
The formatter supports **JSON-based configuration** through Kotlinx Serialization:

```json
{
  "spaceBeforeColon": true,
  "spaceAfterColon": true,
  "maxBlankLines": 1,
  "braceStyle": "same_line",
  "operatorSpacing": true
}
```

### 🔧 FormatterUtil
- **Purpose**: Factory for creating configured formatters
- **Method**: `createFormatter(configText: String, version: String): Formatter`
- **Functionality**: Parses JSON config and creates appropriate formatter instance

## Formatting Rules Overview

### ✅ Spacing Rules
1. **Colons**: Space before and after in type annotations
2. **Operators**: Space before and after arithmetic/assignment operators
3. **Multiple Spaces**: Reduced to single spaces
4. **Function Calls**: Proper spacing around parentheses

### 📄 Line Break Rules
1. **Semicolons**: Newline after statement terminators
2. **Blank Lines**: Maximum one consecutive blank line
3. **Println Statements**: Proper newline placement

### 🏗️ Structural Rules
1. **Braces**: Opening brace on same line as control structure
2. **Indentation**: Consistent indentation (if supported)
3. **Block Formatting**: Proper block statement formatting

## Processing Pipeline

### 1. **Token Input**
```
Tokens: [let, x, :, Number, =, 42, ;, println, (, x, )]
```

### 2. **Sequential Fix Application**
```
Original     → SpaceBeforeColonFix → SpaceAfterColonFix → ... → Final
[let,x,:,...]  [let,x, ,:,...]      [let,x,:, ,...]         [let, ,x,: ,...]
```

### 3. **String Conversion**
```
Formatted Tokens → Converter → "let x: Number = 42;\nprintln(x);"
```

## Usage Examples

### Basic Formatting
```kotlin
val formatter = Formatter()
val tokens = lexer.lex("let x:Number=42;println(x);")
val formatted = formatter.format(tokens)
// Result: "let x: Number = 42;\nprintln(x);"
```

### Configured Formatting
```kotlin
val config = """{"spaceBeforeColon": true, "maxBlankLines": 1}"""
val formatter = FormatterUtil.createFormatter(config, "1.1")
val formatted = formatter.format(tokens)
```

### Error Handling
```kotlin
try {
    val formatted = formatter.format(tokens)
} catch (e: NoSpaceBeforeColonException) {
    println("Formatting error: ${e.message}")
}
```

## Version Support

### PrintScript 1.0
- **Basic Formatting**: Spacing, line breaks, operators
- **Variables**: `let` declaration formatting
- **Functions**: `println` statement formatting

### PrintScript 1.1
- **Extended Support**: `const` declarations, `if` statements
- **Conditional Formatting**: Brace placement for if/else
- **I/O Functions**: `readInput`, `readEnv` formatting

## Code Style Enforcement

### 🎯 Consistency Goals
- **Uniform Spacing**: Consistent whitespace usage
- **Readable Structure**: Clear visual hierarchy
- **Standard Conventions**: Industry-standard formatting rules

### 🔍 Quality Assurance
- **Error Detection**: Identifies formatting violations
- **Automatic Correction**: Fixes issues automatically
- **Configuration**: Customizable rules for different coding standards

## Integration Points

### CLI Integration
```
Source File → Lexer → Tokens → Formatter → Formatted Output
```

### Pipeline Usage
```kotlin
val lexer = LexerUtil.createLexer(version)
val tokens = lexer.lex(sourceCode)
val formatter = FormatterUtil.createFormatter(config, version)
val formatted = formatter.format(tokens)
```

## Dependencies
- **Common Module**: Token interfaces, exceptions
- **Kotlinx Serialization**: JSON configuration support
- **No External Formatting Libraries**: Custom implementation

## Performance Characteristics
- **Time Complexity**: O(n*f) where n is tokens and f is number of fixes
- **Space Complexity**: O(n) for token storage
- **Memory Usage**: Creates new token lists for each fix
- **Configurability**: Flexible rule enabling/disabling

## Error Recovery
- **Graceful Handling**: Continues formatting after recoverable errors
- **Detailed Messages**: Specific error types for different violations
- **Position Tracking**: Maintains source position information for debugging
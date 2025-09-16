# PrintScript Modules Overview

## Project Architecture

PrintScript is a modular interpreter system built with **clean architecture principles** and **separation of concerns**. The system is divided into specialized modules, each handling a specific aspect of the language processing pipeline.

## 📁 Module Structure

```
printscript-tp1/modules/
├── common/              # 🏗️  Shared data structures and utilities
├── lexer/               # 🔤  Lexical analysis (text → tokens)
├── parser/              # 🌳  Syntactic analysis (tokens → AST)
├── interpreter/         # ⚡  Runtime execution (AST → output)
├── formatter/           # 🎨  Code formatting and style
├── linter/              # 🔍  Static code analysis
├── cli/                 # 💻  Command-line interface
├── integration-tests/   # 🧪  End-to-end testing
└── main/                # 📦  Distribution packaging
```

## 🔄 Processing Pipeline

```
Source Code
    ↓
[Lexer] → Tokens
    ↓
[Parser] → AST
    ↓
[Interpreter] → Output
```

**Alternative Paths:**
```
Tokens → [Formatter] → Formatted Code
Tokens → [Linter] → Quality Report
```

## 📋 Module Details

### 🏗️ Common Module
- **Purpose**: Foundation module with shared components
- **Key Components**: AST nodes, tokens, enums, exceptions
- **Dependencies**: None (base module)
- **Role**: Provides contracts and data structures for all other modules

### 🔤 Lexer Module
- **Purpose**: Lexical analysis - converts text to tokens
- **Key Components**: Token rules, pattern matching, version support
- **Dependencies**: Common
- **Features**: String literals, numbers, keywords, operators, identifiers

### 🌳 Parser Module
- **Purpose**: Syntactic analysis - converts tokens to AST
- **Key Components**: Node creators, validators, expression handling
- **Dependencies**: Common
- **Features**: Declarations, assignments, functions, control flow

### ⚡ Interpreter Module
- **Purpose**: Runtime execution - evaluates AST and produces output
- **Key Components**: Value system, environment, built-in functions
- **Dependencies**: Common
- **Features**: Variable management, arithmetic, I/O, type system

### 🎨 Formatter Module
- **Purpose**: Automatic code formatting according to style rules
- **Key Components**: Formatting fixes, configuration, style enforcement
- **Dependencies**: Common, Kotlinx Serialization
- **Features**: Spacing, line breaks, brace placement, customizable rules

### 🔍 Linter Module
- **Purpose**: Static code analysis for quality and style issues
- **Key Components**: Linting rules, violation detection, configuration
- **Dependencies**: Common, Kotlinx Serialization
- **Features**: Naming conventions, style checks, best practices

### 💻 CLI Module
- **Purpose**: Command-line interface for user interaction
- **Key Components**: Commands (execute, lint, format), argument parsing
- **Dependencies**: All other modules, Clikt library
- **Features**: File processing, version management, error handling

### 🧪 Integration Tests Module
- **Purpose**: End-to-end testing of complete workflows
- **Dependencies**: All modules
- **Features**: Full pipeline validation, error handling tests

### 📦 Main Module
- **Purpose**: Distribution packaging and alternative entry points
- **Dependencies**: Core modules
- **Features**: JAR packaging, distribution scripts

## 🎯 Version Support

### PrintScript 1.0
- **Variables**: `let` declarations
- **Types**: Number, String, Boolean, Any
- **Functions**: `println`
- **Operations**: Basic arithmetic and assignment

### PrintScript 1.1 (Extension)
- **Constants**: `const` declarations
- **Conditionals**: `if`/`else` statements
- **I/O Functions**: `readInput`, `readEnv`
- **Boolean Literals**: `true`, `false`

## 🛠️ Technology Stack

### Core Technologies
- **Language**: Kotlin/JVM
- **Build Tool**: Gradle
- **Testing**: JUnit 5, Kotlin Test

### External Dependencies
- **CLI**: Clikt 5.0.1 (command-line parsing)
- **Serialization**: Kotlinx Serialization (JSON config)
- **Code Quality**: KtLint (code formatting)

### Development Tools
- **Linting**: KtLint integration
- **Testing**: Comprehensive test suites
- **Documentation**: Markdown documentation

## 🎨 Design Patterns

### Architectural Patterns
- **Modular Architecture**: Clear separation of concerns
- **Pipeline Pattern**: Sequential processing stages
- **Factory Pattern**: Version-specific component creation

### Implementation Patterns
- **Visitor Pattern**: AST node evaluation (Interpreter)
- **Strategy Pattern**: Configurable rules (Formatter, Linter)
- **Command Pattern**: CLI subcommands
- **Chain of Responsibility**: Sequential rule application

## 🚀 Usage Workflows

### Development Workflow
```bash
# 1. Write PrintScript code
echo 'let x: Number = 42; println(x);' > script.ps

# 2. Lint for quality issues
cli lint script.ps config.json

# 3. Format code
cli format script.ps config.json > formatted.ps

# 4. Execute program
cli execute formatted.ps
```

### Programmatic Usage
```kotlin
// Complete pipeline
val lexer = LexerUtil.createLexer("1.1")
val parser = Parser()
val interpreter = Interpreter()

val tokens = lexer.lex(sourceCode)
val ast = parser.parse(tokens)
val output = interpreter.interpret(ast)
```

## 📊 Quality Assurance

### Testing Strategy
- **Unit Tests**: Individual module functionality
- **Integration Tests**: Cross-module interactions
- **End-to-End Tests**: Complete workflows
- **Edge Case Tests**: Error handling and boundaries

### Code Quality
- **Static Analysis**: KtLint for style consistency
- **Type Safety**: Strong typing throughout
- **Error Handling**: Comprehensive exception system
- **Documentation**: Module-level README files

## 🔮 Extensibility

### Adding New Features
1. **Language Features**: Extend lexer rules and parser node creators
2. **Built-in Functions**: Add to interpreter function evaluation
3. **Formatting Rules**: Create new formatter fixes
4. **Linting Rules**: Implement additional linter rules

### Version Management
- **Backward Compatibility**: New versions extend existing functionality
- **Factory Pattern**: Version-specific component creation
- **Configuration**: JSON-based rule customization

## 📈 Performance Characteristics

### Time Complexity
- **Lexer**: O(n) where n is input length
- **Parser**: O(n) where n is number of tokens
- **Interpreter**: O(n) where n is number of AST nodes
- **Formatter/Linter**: O(n*r) where r is number of rules

### Memory Usage
- **Efficient Processing**: Incremental token and AST processing
- **Scoped Variables**: Environment-based memory management
- **Error Recovery**: Graceful handling of invalid input

## 🎓 Learning Path

For developers working with PrintScript:

1. **Start with Common**: Understand data structures and interfaces
2. **Follow the Pipeline**: Lexer → Parser → Interpreter
3. **Explore Tools**: Formatter and Linter for code quality
4. **Use CLI**: Command-line interface for practical usage
5. **Study Tests**: Integration tests for complete examples

This modular architecture provides a **clean, maintainable, and extensible** foundation for the PrintScript language implementation.

---

# Detailed Module Documentation

## CLI Module

# CLI Module

## Overview
The CLI (Command Line Interface) module provides the **user-facing interface** for the PrintScript interpreter. It allows users to execute, format, and lint PrintScript code through command-line commands. This module serves as the primary entry point for interacting with the PrintScript toolchain.

## Architecture
The CLI uses the **Clikt library** for command-line parsing and follows a **subcommand pattern** where different operations (execute, lint, format) are implemented as separate commands with their own parameters and validation.

## Core Components

### 🎯 Main Classes

#### `Main.kt` (Entry Point)
```kotlin
fun main(args: Array<String>): Unit =
    CommandLineInterface()
        .subcommands(Execute(), Lint(), Format())
        .main(args)
```
- **Purpose**: Application entry point
- **Structure**: Sets up CLI with three main subcommands
- **Framework**: Uses Clikt for command-line processing

#### `CommandLineInterface`
```kotlin
class CommandLineInterface : CliktCommand(name = "cli")
```
- **Purpose**: Root command that coordinates subcommands
- **Pattern**: Container for execute, lint, and format operations
- **Help**: Provides overall CLI help and usage information

### 🔧 Command Implementations
Located in `cli/helper/commands/`:

#### `Execute` Command
```kotlin
class Execute : CliktCommand()
```
- **Purpose**: Executes PrintScript files
- **Usage**: `cli execute <filename> [--version <version>]`
- **Parameters**:
  - `fileName` (required): Path to PrintScript file
  - `--version/-v` (optional): PrintScript version (default: "1.0")
- **Process**:
  1. Reads source file using CliUtil
  2. Creates version-appropriate lexer
  3. Tokenizes code segments (split by semicolons)
  4. Parses tokens into AST
  5. Interprets AST and outputs results

#### `Lint` Command
```kotlin
class Lint : CliktCommand()
```
- **Purpose**: Analyzes code for style and quality issues
- **Usage**: `cli lint <file> <config> [--version <version>]`
- **Parameters**:
  - `file` (required): Path to PrintScript file
  - `config` (required): Path to linter configuration JSON
  - `--version/-v` (optional): PrintScript version (default: "1.0")
- **Process**:
  1. Reads source file and configuration
  2. Creates configured linter for specified version
  3. Tokenizes code segments
  4. Runs linting rules and reports issues

#### `Format` Command
```kotlin
class Format : CliktCommand()
```
- **Purpose**: Formats code according to style guidelines
- **Usage**: `cli format <file> <config> [--version <version>]`
- **Parameters**:
  - `file` (required): Path to PrintScript file
  - `config` (required): Path to formatter configuration JSON
  - `--version/-v` (optional): PrintScript version (default: "1.0")
- **Process**:
  1. Reads source file and configuration
  2. Creates configured formatter for specified version
  3. Tokenizes code (preserving whitespace for formatting)
  4. Applies formatting rules and outputs formatted code

### 🛠️ Utilities
Located in `cli/helper/util/`:

#### `CliUtil`
```kotlin
class CliUtil {
    companion object {
        fun findFile(filename: String): String?
    }
}
```
- **Purpose**: File handling utilities for CLI operations
- **Features**:
  - File existence validation
  - Content reading with error handling
  - User-friendly error messages for missing files

## Command-Line Interface

### 📋 Global Options
- **Help**: `--help` or `-h` displays usage information
- **Version Support**: All commands support `--version/-v` flag

### 🎮 Command Usage Examples

#### Execute PrintScript Code
```bash
# Execute with default version (1.0)
cli execute script.ps

# Execute with specific version
cli execute script.ps --version 1.1

# Execute with version shorthand
cli execute script.ps -v 1.1
```

#### Lint Code Analysis
```bash
# Lint with configuration
cli lint script.ps lint-config.json

# Lint with version specification
cli lint script.ps lint-config.json --version 1.1
```

#### Format Code
```bash
# Format with configuration
cli format script.ps format-config.json

# Format with version specification
cli format script.ps format-config.json -v 1.1
```

#### Help and Documentation
```bash
# General help
cli --help

# Command-specific help
cli execute --help
cli lint --help
cli format --help
```

## Processing Pipeline

### 📊 Execute Command Flow
```
Source File → CliUtil.findFile() → Lexer → Parser → Interpreter → Output
```

### 📏 Lint Command Flow
```
Source File + Config → CliUtil.findFile() → Lexer → Linter → Issues Report
```

### 🎨 Format Command Flow
```
Source File + Config → CliUtil.findFile() → Lexer → Formatter → Formatted Code
```

## Version Management

### 🔄 PrintScript Version Support
- **Version 1.0**: Basic language features
  - Variables (`let`)
  - Basic types (Number, String, Boolean, Any)
  - Functions (`println`)
  - Arithmetic operations

- **Version 1.1**: Extended features
  - Constants (`const`)
  - Conditionals (`if`/`else`)
  - I/O functions (`readInput`, `readEnv`)
  - Boolean literals (`true`/`false`)

### ⚙️ Component Creation
```kotlin
// Version-specific component creation
val lexer = createLexer(version)           // From LexerUtil
val linter = createLinter(config, version) // From LinterUtil
val formatter = createFormatter(config, version) // From FormatterUtil
```

## Error Handling

### 🚨 File Errors
- **`InvalidFileException`**: File not found or inaccessible
- **User Feedback**: Clear error messages with file paths

### 📝 Processing Errors
- **Lexing Errors**: Token recognition issues
- **Parsing Errors**: Syntax problems
- **Runtime Errors**: Execution failures (for execute command)
- **Configuration Errors**: Invalid JSON configuration files

### 💡 Error Recovery
- **Graceful Degradation**: Commands fail with helpful error messages
- **Error Propagation**: Underlying module errors are properly surfaced
- **User Guidance**: Specific error types with actionable advice

## Configuration Files

### 🔧 Linter Configuration (JSON)
```json
{
  "identifier_format": "camel_case",
  "enforce_spacing": true,
  "max_blank_lines": 1,
  "space_before_colon": true,
  "space_after_colon": true
}
```

### 🎨 Formatter Configuration (JSON)
```json
{
  "spaceBeforeColon": true,
  "spaceAfterColon": true,
  "maxBlankLines": 1,
  "braceStyle": "same_line",
  "operatorSpacing": true
}
```

## Implementation Details

### 📦 Dependencies
- **Clikt**: Command-line interface library (`com.github.ajalt.clikt:clikt:5.0.1`)
- **Project Modules**: All other modules (common, lexer, parser, interpreter, linter, formatter)

### 🏗️ Architecture Patterns
- **Subcommand Pattern**: Separate commands for different operations
- **Dependency Injection**: Commands receive dependencies through constructors
- **Error Propagation**: Exceptions bubble up with context
- **Version Abstraction**: Version-specific behavior through utility factories

### 🔄 Code Segmentation
All commands use `segmentsBySemicolon()` to process code:
- **Execute**: Uses standard segmentation for execution
- **Lint**: Processes segments for analysis
- **Format**: Uses `segmentsBySemicolonPreserveWhitespace()` to maintain formatting context

## Testing Strategy

### 🧪 Test Coverage
The CLI module includes comprehensive tests covering:
- **Command instantiation and basic functionality**
- **Argument parsing and validation**
- **Help text generation and documentation**
- **Error handling and edge cases**
- **File operations with various scenarios**
- **Integration workflows and subcommand interaction**

### 📁 Test Structure
```
src/test/kotlin/cli/
├── MainTest.kt                    # Entry point tests
├── helper/
│   ├── CommandLineInterfaceTest.kt # CLI interface tests
│   ├── commands/                   # Command-specific tests
│   └── util/                      # Utility tests
└── integration/                   # Integration tests
```

## Usage Scenarios

### 👨‍💻 Developer Workflow
```bash
# 1. Write PrintScript code
echo 'let x: Number = 42; println(x);' > script.ps

# 2. Check for issues
cli lint script.ps lint-config.json

# 3. Format code
cli format script.ps format-config.json > formatted.ps

# 4. Execute program
cli execute formatted.ps
```

### 🔄 CI/CD Integration
```bash
# Automated code quality checking
cli lint *.ps lint-config.json
if [ $? -eq 0 ]; then
    echo "Linting passed"
    cli execute main.ps
fi
```

## Future Extensions

### 🚀 Potential Enhancements
- **Batch Processing**: Multiple file operations
- **Output Formatting**: JSON, XML output options
- **Interactive Mode**: REPL-style execution
- **Plugin System**: Custom linting/formatting rules
- **IDE Integration**: Language server protocol support

## Dependencies
- **Clikt**: Command-line parsing and help generation
- **Common**: Shared data structures and exceptions
- **Lexer**: Token generation
- **Parser**: AST creation
- **Interpreter**: Code execution
- **Linter**: Code analysis
- **Formatter**: Code formatting

## Module Integration
```
User Input → [CLI] → Module Coordination → User Output
                ↓
         File I/O & Processing
```

The CLI serves as the orchestration layer that coordinates all other modules to provide a complete PrintScript development experience through command-line tools.

---

## Common Module

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
```
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

---

## Formatter Module

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

---

## Integration Tests Module

# Integration Tests Module

## Overview
The Integration Tests module contains **end-to-end integration tests** for the PrintScript system. These tests validate the complete workflow from source code input through lexing, parsing, and execution to final output.

## Purpose
- **Full Pipeline Testing**: Tests the complete compilation and execution pipeline
- **Cross-Module Validation**: Ensures all modules work together correctly
- **Regression Prevention**: Catches issues that might arise from module interactions
- **Quality Assurance**: Validates the overall system behavior

## Architecture
This appears to be a **build-only module** that contains compiled test classes and test reports rather than source code. The actual integration test source files are likely located in the CLI module (`cli/src/test/kotlin/integration/PrintScriptIntegrationTest.kt`).

## Test Scope

### 🔄 End-to-End Workflows
- **Complete Execution**: Source code → Lexer → Parser → Interpreter → Output
- **Error Handling**: Invalid syntax and runtime error propagation
- **Version Compatibility**: Testing both PrintScript 1.0 and 1.1 features

### 📊 Integration Points
- **Lexer + Parser**: Token stream to AST conversion
- **Parser + Interpreter**: AST execution and output generation
- **CLI Integration**: Command-line interface workflows
- **Configuration**: Formatter and linter configuration processing

## Test Categories

### ✅ Functional Tests
- Variable declaration and assignment
- Arithmetic operations and string concatenation
- Function calls (println, readInput, readEnv)
- Conditional statements (if/else)
- Type system validation

### 🚨 Error Handling Tests
- Syntax errors and malformed code
- Runtime errors (division by zero, undefined variables)
- Type mismatches and invalid operations
- File access errors

### 🔧 Tool Integration Tests
- Execute command functionality
- Lint command with various configurations
- Format command with different style rules
- Version-specific feature testing

## Build Artifacts
Located in the `build/` directory:
- **Compiled Test Classes**: `.class` files for test execution
- **Test Reports**: HTML and XML test result reports
- **JAR Files**: Packaged test artifacts
- **Temporary Files**: Build process intermediates

## Dependencies
- **All Project Modules**: Tests require the complete system
- **JUnit 5**: Testing framework
- **Kotlin Test**: Kotlin-specific testing utilities

## Usage
Integration tests are typically run as part of the build process:
```bash
./gradlew :integration-tests:test
```

## Module Role
This module serves as the **quality gate** for the entire PrintScript system, ensuring that all components work together seamlessly to provide the expected user experience.

## Note
This module appears to contain only build artifacts. The actual integration test source code may be distributed across other modules, particularly in the CLI module's integration test directory.

---

## Interpreter Module

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

---

## Lexer Module

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

---

## Linter Module

# Linter Module

## Overview
The Linter module is responsible for **static code analysis** in PrintScript. It examines tokenized code to detect potential issues, enforce coding standards, and identify violations of best practices without executing the code. The linter helps maintain code quality and consistency across PrintScript projects.

## Architecture
The linter follows a **rule-based analysis pattern** where individual linting rules are applied to the token stream. Each rule examines specific aspects of the code and reports violations as exceptions. This modular approach allows for flexible rule configuration and easy extension.

## Core Components

### 🎯 Main Classes

#### `Linter` (Primary Interface)
```kotlin
class Linter(private val linterRules: List<LinterRule>)
```
- **Purpose**: Orchestrates the static analysis process
- **Key Method**: `lint(tokens: List<TokenInterface>): List<Throwable>`
- **Process**:
  1. Applies each linting rule to the token stream
  2. Collects violations from all rules
  3. Returns comprehensive list of issues found
  4. Non-blocking: continues analysis after finding issues

#### `LinterRule` (Abstract Base)
```kotlin
abstract class LinterRule {
    abstract fun match(tokens: List<TokenInterface>): List<Throwable>?
}
```
- **Purpose**: Base interface for all linting rules
- **Pattern**: Analyzes token stream and returns violations
- **Flexibility**: Rules can examine local or global token patterns

### 📏 Linting Rule System
Located in `linter/rules/`:

#### Core Rule Categories

##### Naming Convention Rules
- **Identifier Formatting**: Variable and function name conventions
- **Type Naming**: Class and type name standards
- **Constant Naming**: Special naming rules for constants

##### Code Style Rules
- **Spacing**: Whitespace and indentation standards
- **Line Length**: Maximum line length enforcement
- **Bracket Placement**: Brace and parenthesis positioning

##### Best Practice Rules
- **Variable Usage**: Unused variable detection
- **Type Safety**: Potential type issues
- **Code Complexity**: Overly complex expressions

### 🔧 Configuration System

#### `LinterUtil`
- **Purpose**: Factory for creating configured linter instances
- **Method**: `createLinter(configPath: String, version: String): Linter`
- **Configuration**: JSON-based rule configuration

#### JSON Configuration Format
```json
{
  "identifier_format": "camel_case",
  "enforce_spacing": true,
  "max_line_length": 120,
  "allow_unused_variables": false,
  "enforce_type_annotations": true
}
```

## Rule Categories

### 🏷️ Naming Convention Rules

#### Identifier Format Rules
- **Camel Case**: `variableName`, `functionName`
- **Snake Case**: `variable_name`, `function_name`
- **Pascal Case**: `TypeName`, `ClassName`

#### Validation Examples
```
// Camel case validation
let userName = "john";     // ✓ Valid
let user_name = "john";    // ✗ InvalidCamelCaseException

// Constant naming
const MAX_SIZE = 100;      // ✓ Valid (constants often UPPER_CASE)
const maxSize = 100;       // ✓ Valid (depending on configuration)
```

### 📐 Code Style Rules

#### Spacing Rules
- **Operator Spacing**: Spaces around operators
- **Colon Spacing**: Spaces around type annotations
- **Function Spacing**: Spaces in function calls

#### Line Break Rules
- **Statement Separation**: Proper line breaks between statements
- **Block Formatting**: Consistent block indentation
- **Blank Line Limits**: Maximum consecutive empty lines

### 🔍 Semantic Analysis Rules

#### Variable Usage
- **Unused Variables**: Detection of declared but unused variables
- **Uninitialized Access**: Variables used before initialization
- **Scope Violations**: Variables accessed outside their scope

#### Type Safety
- **Type Consistency**: Consistent type usage patterns
- **Invalid Conversions**: Potentially problematic type conversions
- **Missing Annotations**: Required type annotations

## Analysis Process

### 1. **Token Stream Analysis**
```
Input Tokens → Rule Application → Issue Collection → Report
```

### 2. **Rule Execution**
```
For each rule:
  1. Examine token patterns
  2. Identify violations
  3. Create specific exception
  4. Continue with next rule
```

### 3. **Issue Aggregation**
```
All Issues → Categorization → Priority Sorting → Final Report
```

## Exception System

### 🚨 Linting Exceptions
The linter uses the same exception system as the formatter:

#### Naming Violations
- **`InvalidCamelCaseException`**: Identifier doesn't follow camelCase
- **`InvalidPascalCaseException`**: Type name doesn't follow PascalCase
- **`InvalidSnakeCaseException`**: Identifier doesn't follow snake_case

#### Spacing Violations
- **`NoSpaceBeforeColonException`**: Missing space before type colon
- **`NoSpaceAfterColonException`**: Missing space after type colon
- **`NoSpaceBeforeOperatorException`**: Missing space before operators
- **`NoSpaceAfterOperatorException`**: Missing space after operators
- **`MoreThanOneSpaceAfterTokenException`**: Excessive spacing

#### Structural Violations
- **`InvalidIfBracePlacementException`**: Incorrect brace placement
- **`TooManyBlankLinesException`**: Excessive blank lines
- **`NoNewLineAfterSemiColon`**: Missing line break after semicolon

## Configuration Options

### 📋 Rule Configuration

#### Naming Conventions
```json
{
  "identifier_format": "camel_case|snake_case|pascal_case",
  "constant_format": "upper_case|camel_case",
  "enforce_type_names": true
}
```

#### Spacing Standards
```json
{
  "space_before_colon": true,
  "space_after_colon": true,
  "operator_spacing": true,
  "max_consecutive_spaces": 1
}
```

#### Structural Rules
```json
{
  "brace_style": "same_line|new_line",
  "max_blank_lines": 1,
  "require_semicolon_newline": true
}
```

## Usage Examples

### Basic Linting
```kotlin
val linter = Linter(defaultRules)
val tokens = lexer.lex(sourceCode)
val issues = linter.lint(tokens)

issues.forEach { issue ->
    println("Issue: ${issue.message}")
}
```

### Configured Linting
```kotlin
val config = """{"identifier_format": "camel_case", "enforce_spacing": true}"""
val linter = LinterUtil.createLinter(config, "1.1")
val issues = linter.lint(tokens)
```

### Issue Handling
```kotlin
val issues = linter.lint(tokens)
val errors = issues.filterIsInstance<InvalidCamelCaseException>()
val warnings = issues.filterIsInstance<TooManyBlankLinesException>()

println("Found ${errors.size} naming errors")
println("Found ${warnings.size} style warnings")
```

## Rule Implementation

### 🔧 Custom Rule Development
```kotlin
class CustomNamingRule : LinterRule() {
    override fun match(tokens: List<TokenInterface>): List<Throwable>? {
        val violations = mutableListOf<Throwable>()

        // Analyze tokens for violations
        tokens.forEach { token ->
            if (violatesNamingConvention(token)) {
                violations.add(InvalidCamelCaseException("Bad name: ${token.value}"))
            }
        }

        return violations.ifEmpty { null }
    }
}
```

### 📊 Rule Priority
Rules are typically organized by severity:
1. **Error Level**: Syntax violations, critical issues
2. **Warning Level**: Style violations, best practice issues
3. **Info Level**: Suggestions, minor improvements

## Integration Points

### CLI Integration
```
Source File → Lexer → Tokens → Linter → Issue Report
```

### Pipeline Usage
```kotlin
val lexer = LexerUtil.createLexer(version)
val linter = LinterUtil.createLinter(configPath, version)

val tokens = lexer.lex(sourceCode)
val issues = linter.lint(tokens)

if (issues.isNotEmpty()) {
    issues.forEach { println("Lint issue: ${it.message}") }
}
```

## Version Support

### PrintScript 1.0
- **Basic Linting**: Variable naming, spacing, basic structure
- **Core Rules**: Essential code quality checks
- **Simple Expressions**: Arithmetic and assignment validation

### PrintScript 1.1
- **Extended Analysis**: Constants, conditionals, I/O functions
- **Advanced Rules**: Block structure, scope analysis
- **Enhanced Validation**: More sophisticated pattern detection

## Quality Assurance Features

### 🎯 Code Quality Metrics
- **Consistency**: Uniform code style enforcement
- **Readability**: Clear and maintainable code patterns
- **Best Practices**: Industry-standard coding conventions

### 📈 Reporting Features
- **Detailed Messages**: Specific violation descriptions
- **Position Information**: Line/column location of issues
- **Severity Levels**: Categorized issue importance
- **Batch Analysis**: Multiple file processing support

## Dependencies
- **Common Module**: Token interfaces, exception types
- **Kotlinx Serialization**: JSON configuration parsing
- **No External Analysis Tools**: Custom implementation

## Performance Characteristics
- **Time Complexity**: O(n*r) where n is tokens and r is number of rules
- **Space Complexity**: O(i) where i is number of issues found
- **Memory Usage**: Efficient token stream processing
- **Scalability**: Handles large codebases effectively

## Error Recovery
- **Non-blocking Analysis**: Continues after finding issues
- **Comprehensive Reporting**: Collects all violations before reporting
- **Graceful Degradation**: Handles malformed tokens without crashing
- **Rule Isolation**: Individual rule failures don't affect others

---

## Main Module

# Main Module

## Overview
The Main module appears to be a **legacy or alternative entry point** for the PrintScript system. This module contains build artifacts but no apparent source code, suggesting it may have been replaced by the CLI module or serves as a distribution packaging point.

## Purpose
Based on the build artifacts, this module likely serves one of these purposes:
- **Legacy Entry Point**: Original main class before CLI module development
- **Distribution Packaging**: Creates distributable JARs and scripts
- **Build Coordination**: Orchestrates the overall build process
- **Alternative Interface**: Non-CLI interface for PrintScript

## Architecture
This is a **build-only module** containing:
- Compiled Kotlin classes (`MainKt.class`)
- Distribution archives (`.tar`, `.zip`)
- Executable scripts (`main`, `main.bat`)
- JAR files for deployment

## Build Artifacts

### 📦 Distribution Files
- **Archives**: `main-1.0.0.tar`, `main-1.0.0.zip` - Complete distribution packages
- **JARs**: `main-1.0.0.jar` - Executable JAR file
- **Scripts**: `main`, `main.bat` - Platform-specific launch scripts

### 🔧 Build Metadata
- **Kotlin Compilation**: Cached compilation data and metadata
- **Manifest**: JAR manifest with entry point information
- **Reports**: KtLint formatting and style reports

## Potential Functionality
Based on the naming and structure, this module might provide:

### 🚀 Alternative Entry Points
- **Simple Execution**: Direct PrintScript execution without CLI complexity
- **Library Usage**: API for embedding PrintScript in other applications
- **Batch Processing**: Non-interactive script execution

### 📋 Distribution Support
- **Standalone JAR**: Self-contained executable for deployment
- **Cross-Platform Scripts**: Launch scripts for different operating systems
- **Package Management**: Structured distribution for installation

## Relationship to CLI Module
The CLI module (`modules/cli/`) appears to be the **primary interface** for PrintScript, while this main module might be:
- **Deprecated**: Replaced by the more feature-rich CLI module
- **Specialized**: Focused on specific use cases not covered by CLI
- **Alternative**: Different interface paradigm (API vs CLI)

## Usage Scenarios

### 🔄 Programmatic Access
```kotlin
// Hypothetical usage if this were a library interface
val printScript = PrintScript()
val result = printScript.execute("let x: Number = 42; println(x);")
```

### 📦 Distribution Deployment
```bash
# Using distribution archives
tar -xf main-1.0.0.tar
cd main-1.0.0/
./bin/main script.ps
```

### 🚀 JAR Execution
```bash
# Direct JAR execution
java -jar main-1.0.0.jar script.ps
```

## Dependencies
- **Project Modules**: Likely depends on core modules (common, lexer, parser, interpreter)
- **Kotlin Runtime**: Standard Kotlin/JVM dependencies
- **Distribution Tools**: Gradle application plugin for packaging

## Module Status
This module appears to be in a **maintenance or legacy state**:
- Contains only build artifacts
- No visible source code
- May be superseded by CLI module
- Possibly used for distribution packaging only

## Integration Points
```
Main Module → [Distribution] → End Users
            → [API] → Other Applications
            → [Legacy] → Backward Compatibility
```

## Note
This module may serve primarily as a **distribution and packaging point** rather than containing active source code. The primary development and functionality appears to be concentrated in the CLI module, which provides a comprehensive command-line interface for all PrintScript operations.

## Recommendations
- **Clarify Purpose**: Determine if this module is still actively used
- **Documentation**: Add source-level documentation if functionality exists
- **Consolidation**: Consider merging with CLI if redundant
- **Distribution**: Leverage for creating user-friendly distributions

---

## Parser Module

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
Tokens → [Parser] → AST → [Interpreter]
```

The parser serves as the bridge between lexical analysis and semantic processing, providing a structured representation that other modules can work with efficiently.

## Performance Characteristics
- **Time Complexity**: O(n) where n is the number of tokens
- **Space Complexity**: O(d) where d is the AST depth
- **Memory Usage**: Creates AST nodes incrementally
- **Error Recovery**: Fails fast with detailed error information
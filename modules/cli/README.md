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
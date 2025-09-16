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
```kotlin
// Camel case validation
"let userName = "john";"     // ✓ Valid
"let user_name = "john";"    // ✗ InvalidCamelCaseException

// Constant naming
"const MAX_SIZE = 100;"      // ✓ Valid (constants often UPPER_CASE)
"const maxSize = 100;"       // ✓ Valid (depending on configuration)
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
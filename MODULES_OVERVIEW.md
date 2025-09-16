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

## 🔗 Dependency Graph

```
                    common
                      ↑
        ┌─────────────┼─────────────┐
        ↓             ↓             ↓
     lexer ────→   parser ────→ interpreter
        ↓             ↓             ↓
        └─────────────┼─────────────┘
                      ↓
               ┌──────┼──────┐
               ↓      ↓      ↓
           formatter linter cli
               ↓      ↓      ↓
               └──────┼──────┘
                      ↓
              integration-tests
```

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
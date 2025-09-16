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
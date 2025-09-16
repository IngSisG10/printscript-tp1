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
# Troubleshooting Guide

## Understanding the Project Structure

This project demonstrates how to build command-line tools in Kotlin and compile them to native binaries using GraalVM. It includes multiple implementations with different approaches to handle potential GraalVM limitations.

## Available Implementations

### 1. Ultra Simple GitHub App (`UltraSimpleGithubApp.kt`)

**Description:** The most reliable implementation that uses only Java standard library with simple string operations for JSON parsing.

**Key Features:**
- No external dependencies
- No regex
- Manual string parsing of JSON
- Highly compatible with GraalVM

**Build with:** `./build-ultra-simple.sh` or `./build-both-simple.sh`

### 2. Basic GitHub App (`BasicGithubApp.kt`)

**Description:** Uses the org.json library for JSON parsing which is generally well-supported by GraalVM.

**Key Features:**
- Uses org.json for parsing
- No GSON reflection issues
- More robust JSON parsing than string manipulation

**Build with:** `./build-basic.sh`

### 3. Improved GitHub App (`ImprovedGithubApp.kt`)

**Description:** Uses Gson but with a more robust approach to handle deserialization issues.

**Key Features:**
- Uses a Map-based approach with Gson
- More flexible than direct object mapping

**Build with:** `./build-robust.sh`

### 4. Other Implementations

- `SimpleGithubApp2.kt`: Uses Gson with TypeToken
- `x9Cli.kt`: The original picocli-based implementation

## Common Issues and Solutions

### 1. Gson Deserialization Issues

**Problem:** `Unable to create instance of class X. Registering an InstanceCreator or a TypeAdapter for this type, or adding a no-args constructor may fix this problem.`

**Solutions:**
- Use the UltraSimpleGithubApp implementation
- Add no-args constructors to data classes
- Use Map-based deserialization

### 2. Regex Issues in Native Image

**Problem:** Regular expressions can sometimes cause issues in native image compilation.

**Solution:** Use simple string operations instead (indexOf, substring) as shown in UltraSimpleGithubApp.

### 3. Reflection Configuration

**Problem:** GraalVM needs to know at build time which classes will be accessed via reflection.

**Solution:** Provide reflection configuration in `META-INF/native-image/reflect-config.json` or avoid reflection entirely.

### 4. Library Compatibility

**Problem:** Some libraries may not work well with GraalVM native image.

**Solution:** Use the most basic Java standard library classes when possible, or libraries known to work well with GraalVM.

## Testing Your Implementation

Before building the native image, you can test your implementation directly with Java:

```bash
./test-ultra-simple.sh octocat
```

This will run the UltraSimpleGithubApp directly using the JVM.

## Build Scripts

- `build-ultra-simple.sh`: Builds just the ultra simple GitHub app
- `build-both-simple.sh`: Builds both the checksum and ultra simple GitHub apps
- `build-basic.sh`: Builds the org.json implementation
- `build-robust.sh`: Builds the improved Gson implementation
- `build-no-deps.sh`: Another simplified approach
- `build-bare-minimum.sh`: Minimal build configuration
- `build-simple.sh`: Original build approach

## Recommended Approach

For maximum compatibility with GraalVM, use the UltraSimpleGithubApp implementation with the `build-ultra-simple.sh` or `build-both-simple.sh` scripts.

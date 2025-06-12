# Picocli Checksum Calculator

A command-line utility built with Kotlin and Picocli that calculates file checksums using various algorithms.

## Overview

This project demonstrates how to build a command-line application using:
- Kotlin 2.1.20
- Picocli 4.7.7 for command-line argument parsing
- Java SDK 24 compatibility
- Maven for dependency management and building

## Features

- Calculate checksums using different algorithms (MD5, SHA-1, SHA-256, etc.)
- User-friendly command-line interface with help and version information
- Simple file input handling

## Prerequisites

- Java SDK 24 or later
- Maven 3.6 or later

## Building the Project

```bash
mvn clean package
```

## Running the Application

### Using Maven

```bash
mvn exec:java -Dexec.args="--algorithm SHA-1 hello.txt"
```

### Using Java directly

```bash
java -classpath target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) ChecksumKt --algorithm SHA-1 hello.txt
```

### Manual classpath method

```
java -classpath /Users/lucasdamaceno/Documents/pocs/picocli-poc/target/classes:/Users/lucasdamaceno/.m2/repository/org/jetbrains/kotlin/kotlin-stdlib/2.1.20/kotlin-stdlib-2.1.20.jar:/Users/lucasdamaceno/.m2/repository/org/jetbrains/annotations/13.0/annotations-13.0.jar:/Users/lucasdamaceno/.m2/repository/info/picocli/picocli/4.7.7/picocli-4.7.7.jar ChecksumKt --algorithm SHA-1 hello.txt
```

## Command-Line Options

- `-a, --algorithm <ALGORITHM>` - Specify the hashing algorithm (default: SHA-256)
- `-h, --help` - Show help message and exit
- `-V, --version` - Print version information and exit

## Examples

Calculate SHA-256 checksum (default):
```bash
java -classpath ... ChecksumKt hello.txt
```

Calculate MD5 checksum:
```bash
java -classpath ... ChecksumKt --algorithm MD5 hello.txt
```

## License

This project is available under the MIT License.
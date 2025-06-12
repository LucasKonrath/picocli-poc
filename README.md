# Picocli Tools

A collection of command-line utilities built with Kotlin and Picocli.

## Tools Included

1. **Checksum Calculator** - Calculate file checksums using various algorithms
2. **x9** - GitHub activity viewer that summarizes a user's push events

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

# x9 GitHub Activity Viewer

The x9 command helps you track GitHub activity for a specified user by retrieving and displaying their recent push events and commits.

## Using x9

### Running via Maven

```bash
mvn exec:java -Dexec.mainClass="X9CliKt" -Dexec.args="username"
```

### Running via Java

```bash
java -classpath target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) X9CliKt username
```

### Command-Line Options

- `username` - The GitHub username whose activity you want to view (required)
- `-h, --help` - Show help message and exit
- `-V, --version` - Print version information and exit

## Creating Command Aliases

### Unix/Linux/MacOS

Add the following to your `~/.bashrc`, `~/.zshrc`, or equivalent shell configuration file:

```bash
# For the Checksum command
alias checksum='java -classpath /path/to/project/target/classes:/path/to/required/jars ChecksumKt'

# For the x9 command
alias x9='java -classpath /path/to/project/target/classes:/path/to/required/jars X9CliKt'
```

Then reload your configuration:

```bash
source ~/.bashrc  # or ~/.zshrc
```

### Windows (PowerShell)

Add the following to your PowerShell profile:

```powershell
function checksum { java -classpath "C:\path\to\project\target\classes;C:\path\to\required\jars" ChecksumKt $args }
function x9 { java -classpath "C:\path\to\project\target\classes;C:\path\to\required\jars" X9CliKt $args }
```

### Windows (Command Prompt)

Create a batch file named `checksum.bat` and `x9.bat` in a directory that's in your PATH:

```batch
@echo off
java -classpath "C:\path\to\project\target\classes;C:\path\to\required\jars" ChecksumKt %*
```

```batch
@echo off
java -classpath "C:\path\to\project\target\classes;C:\path\to\required\jars" X9CliKt %*
```

## Examples

View GitHub activity for a user:
```bash
x9 octocat
```

## License

This project is available under the MIT License.
#!/bin/bash

# Make script executable
chmod +x "$0"

# First, clean and package the project
mvn clean package

# Define variables
JAR_FILE="target/picocli-poc-1.0-SNAPSHOT-jar-with-dependencies.jar"

# Create simple reflection configuration directly in this script
cat > reflect-config.json << 'EOL'
[
  {
    "name": "picocli.CommandLine$AutoHelpMixin",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allPublicMethods": true
  },
  {
    "name": "Checksum",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allPublicMethods": true,
    "allDeclaredFields": true
  },
  {
    "name": "x9Cli",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allPublicMethods": true,
    "allDeclaredFields": true
  },
  {
    "name": "java.io.File",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true
  }
]
EOL

# Build the checksum native image
echo "Building checksum native image..."
native-image \
  --no-fallback \
  --initialize-at-build-time=kotlin \
  --initialize-at-run-time=picocli.CommandLine\$Tracer \
  -H:ReflectionConfigurationFiles=reflect-config.json \
  -H:+ReportExceptionStackTraces \
  -cp $JAR_FILE \
  ChecksumKt \
  checksum

# Build the x9 native image
echo "Building x9 native image..."
native-image \
  --no-fallback \
  --initialize-at-build-time=kotlin \
  --initialize-at-run-time=picocli.CommandLine\$Tracer \
  --initialize-at-run-time=okhttp3 \
  -H:ReflectionConfigurationFiles=reflect-config.json \
  -H:+ReportExceptionStackTraces \
  -cp $JAR_FILE \
  X9CliKt \
  x9

echo "Build completed. Executables 'checksum' and 'x9' are ready to use."

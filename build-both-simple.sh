#!/bin/bash

# Make script executable
chmod +x "$0"

# Clean and package the project
mvn clean package

# Get the JAR location
JAR_FILE="target/picocli-poc-1.0-SNAPSHOT-jar-with-dependencies.jar"

# Build the simple checksum native image
echo "Building simple checksum native image..."
native-image \
  -cp $JAR_FILE \
  SimpleChecksumKt \
  checksum

# Build the ultra simple GitHub app native image
echo "Building ultra simple GitHub app native image..."
native-image \
  -cp $JAR_FILE \
  UltraSimpleGithubAppKt \
  x9

echo "Build completed. Executables 'checksum' and 'x9' are ready to use."

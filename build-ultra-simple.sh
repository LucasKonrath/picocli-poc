#!/bin/bash

# Make script executable
chmod +x "$0"

# Clean and package the project
mvn clean package

# Get the JAR location
JAR_FILE="target/picocli-poc-1.0-SNAPSHOT-jar-with-dependencies.jar"

# Build the ultra simple GitHub app native image
echo "Building ultra simple GitHub app native image..."
native-image \
  -cp $JAR_FILE \
  --enable-url-protocols=https \
  UltraSimpleGithubAppKt \
  x9

echo "Build completed. Executable 'x9' is ready to use."

#!/bin/bash

# Make script executable
chmod +x "$0"

# Clean and package the project
mvn clean package

# Get the JAR location
JAR_FILE="target/picocli-poc-1.0-SNAPSHOT-jar-with-dependencies.jar"

# Build the basic GitHub app native image with org.json
echo "Building basic GitHub app native image..."
native-image \
  --no-fallback \
  -H:+ReportExceptionStackTraces \
  -cp $JAR_FILE \
  BasicGithubAppKt \
  x9

echo "Build completed. Executable 'x9' is ready to use."

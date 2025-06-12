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
  --no-fallback \
  --allow-incomplete-classpath \
  --report-unsupported-elements-at-runtime \
  -H:+ReportExceptionStackTraces \
  -cp $JAR_FILE \
  org.example.checksum.SimpleAppKt \
  checksum

# Build the basic GitHub app native image with org.json
echo "Building basic GitHub app native image..."
native-image \
  --no-fallback \
  --allow-incomplete-classpath \
  --report-unsupported-elements-at-runtime \
  -H:+ReportExceptionStackTraces \
  -cp $JAR_FILE \
  BasicGithubAppKt \
  x9

echo "Build completed. Executables 'checksum' and 'x9' are ready to use."

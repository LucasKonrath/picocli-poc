#!/bin/bash

# Make script executable
chmod +x "$0"

# Compile
mvn clean compile

# Compile SimpleChecksum manually
kotlinc src/main/kotlin/SimpleChecksum.kt -cp target/classes:$HOME/.m2/repository/info/picocli/picocli/4.7.7/picocli-4.7.7.jar -d target/classes

# Package
mvn package

# Build native image directly
echo "Building minimal native image..."
native-image \
  --no-fallback \
  --initialize-at-run-time=picocli.CommandLine\$Tracer \
  -cp target/picocli-poc-1.0-SNAPSHOT-jar-with-dependencies.jar \
  SimpleChecksumKt \
  simple-checksum

echo "Build completed. Executable 'simple-checksum' is ready to use."

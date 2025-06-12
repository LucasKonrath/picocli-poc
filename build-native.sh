#!/bin/bash

# Make this script executable
chmod +x "$0"

# Ensure we have a clean build
mvn clean package

# Get the JAR location
JAR_FILE="target/picocli-poc-1.0-SNAPSHOT-jar-with-dependencies.jar"
REFLECT_CONFIG="src/main/resources/META-INF/native-image/picocli-reflect-config.json"

# Build checksum native image
echo "Building checksum native image..."
native-image --no-fallback \
  -cp $JAR_FILE \
  -H:ReflectionConfigurationFiles=$REFLECT_CONFIG \
  -H:Name=checksum \
  --initialize-at-build-time=picocli.CommandLine \
  --initialize-at-build-time=kotlin \
  --initialize-at-build-time=org.jetbrains \
  --initialize-at-build-time=com.google.gson \
  --initialize-at-run-time=okhttp3 \
  --report-unsupported-elements-at-runtime \
  --allow-incomplete-classpath \
  -H:+ReportExceptionStackTraces \
  ChecksumKt

# Build x9 native image
echo "Building x9 native image..."
native-image --no-fallback \
  -cp $JAR_FILE \
  -H:ReflectionConfigurationFiles=$REFLECT_CONFIG \
  -H:Name=x9 \
  --initialize-at-build-time=picocli.CommandLine \
  --initialize-at-build-time=kotlin \
  --initialize-at-build-time=org.jetbrains \
  --initialize-at-build-time=com.google.gson \
  --initialize-at-run-time=okhttp3 \
  --report-unsupported-elements-at-runtime \
  --allow-incomplete-classpath \
  -H:+ReportExceptionStackTraces \
  X9CliKt

# Make them executable
chmod +x checksum x9

# Move to target directory
mv checksum x9 target/

echo "Build complete. Executables are in target/ directory."

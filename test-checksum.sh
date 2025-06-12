#!/bin/bash

# Make script executable
chmod +x "$0"

# Compile the Java code
mvn clean compile

# Create a test file
echo "This is a test file for checksum calculation." > test-file.txt

# Run the simple checksum implementation directly
echo "Running SimpleChecksum directly..."
java -cp target/classes SimpleChecksumKt test-file.txt

# Run with MD5 algorithm
echo "\nRunning with MD5 algorithm..."
java -cp target/classes SimpleChecksumKt -a MD5 test-file.txt

# Clean up
rm test-file.txt

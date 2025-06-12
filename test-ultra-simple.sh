#!/bin/bash

# Make script executable
chmod +x "$0"

# Compile the Java code
mvn clean compile

# Run the ultra simple implementation directly
echo "Running UltraSimpleGithubApp directly..."
java -cp target/classes UltraSimpleGithubAppKt $@

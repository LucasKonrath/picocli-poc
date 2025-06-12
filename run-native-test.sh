#!/bin/bash

# Create a test file
echo "Hello, world!" > hello.txt

# Run checksum on it
echo "Running checksum on hello.txt:"
./target/checksum -a SHA-256 hello.txt

# Run x9 to fetch github data
echo "\nRunning x9 to fetch GitHub data for octocat:"
./target/x9 octocat

echo "\nTests completed!"

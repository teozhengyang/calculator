#!/bin/bash

# Navigate to the directory containing Calculator.java
cd "$(dirname "$0")"

# Compile the Calculator.java file
javac Calculator.java

# Run the Calculator program
java Calculator
package org.example.checksum

import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.security.MessageDigest

/**
 * A super simple implementation of the checksum functionality
 * that avoids all picocli reflection issues
 */
fun main(args: Array<String>) {
    // Parse args manually
    if (args.isEmpty() || args.contains("-h") || args.contains("--help")) {
        printChecksumHelp()
        return
    }

    if (args.contains("-V") || args.contains("--version")) {
        println("checksum 4.0")
        return
    }

    var algorithm = "SHA-256"
    var filePath = ""

    var i = 0
    while (i < args.size) {
        when {
            args[i] == "-a" || args[i] == "--algorithm" -> {
                if (i + 1 < args.size) {
                    algorithm = args[i + 1]
                    i += 2
                } else {
                    println("Error: Missing algorithm value")
                    return
                }
            }
            args[i].startsWith("-") -> {
                println("Unknown option: ${args[i]}")
                return
            }
            else -> {
                filePath = args[i]
                i++
            }
        }
    }

    if (filePath.isEmpty()) {
        println("Error: Missing file path")
        return
    }

    try {
        val file = File(filePath)
        if (!file.exists()) {
            println("Error: File not found: $filePath")
            return
        }

        val fileContents = Files.readAllBytes(file.toPath())
        val digest = MessageDigest.getInstance(algorithm).digest(fileContents)
        println(("%0" + digest.size * 2 + "x").format(BigInteger(1, digest)))
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun printChecksumHelp(): Unit {
    println("Usage: checksum [-a ALGORITHM] FILE")
    println("Prints the checksum of a file to STDOUT.")
    println()
    println("Options:")
    println("  -a, --algorithm ALGORITHM   MD5, SHA-1, SHA-256, ... (default: SHA-256)")
    println("  -h, --help                  Show this help message and exit")
    println("  -V, --version               Print version information and exit")
}
